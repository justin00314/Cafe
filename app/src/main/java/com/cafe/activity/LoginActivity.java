package com.cafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafe.R;
import com.cafe.common.CommonUtils;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.LoginContract;
import com.cafe.data.account.LoginRequest;
import com.cafe.presenter.LoginPresenter;

import org.justin.utils.thread.ThreadUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.*;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;


public class LoginActivity extends MVPActivity<LoginContract.View,
        LoginContract.Presenter> implements LoginContract.View, EasyPermissions.PermissionCallbacks {

    private static final int PERMISSION_REQUEST_CODE = 999;

    private EditText mUserName;
    private EditText mPassword;

    private String[] mPerms = {CAMERA, RECORD_AUDIO};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        supportToolbar(false);

        ImageView userImage = (ImageView) findViewById(R.id.user_name).findViewById(R.id.editor_icon);
        userImage.setImageResource(R.mipmap.icon_people);
        ImageView passwordImage = (ImageView) findViewById(R.id.user_password).findViewById(R.id.editor_icon);
        passwordImage.setVisibility(View.GONE);

        mUserName = (EditText) findViewById(R.id.user_name).findViewById(R.id.editor_content);

        mPassword = (EditText) findViewById(R.id.user_password).findViewById(R.id.editor_content);
        mPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    login(null);
                    return true;
                }

                return false;
            }
        });

        TextInputLayout userName = (TextInputLayout) findViewById(R.id.user_name).findViewById(R.id.editor_layout);
        userName.setHint(getString(R.string.user_name));
        TextInputLayout passwordText = (TextInputLayout) findViewById(R.id.user_password).findViewById(R.id.editor_layout);
        passwordText.setHint(getString(R.string.password));

        checkPermission();
    }

    @Override
    public LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    public void login(View v) {
        if (validateInput()) {
            LoginRequest request = new LoginRequest();
            request.password = mPassword.getText().toString();
            request.userName = mUserName.getText().toString();
            getPresenter().login(request);
        } else {
            Toast.makeText(this, R.string.name_password_not_empty, Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    public void loginDone(boolean success, String errorMessage) {
        if (success) {
            Intent intent = new Intent(this, MeetingListActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(R.id.container_layout), errorMessage, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    private void methodRequiresPermission() {
        if (EasyPermissions.hasPermissions(this, mPerms)) {

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.ensure))
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setRequestCode(PERMISSION_REQUEST_CODE)
                    .build()
                    .show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    private boolean validateInput() {
        boolean validate = false;

        if (mUserName.getText() != null && mPassword.getText() != null) {
            if (!mUserName.getText().toString().trim().isEmpty() && !mPassword.getText().toString().trim().isEmpty()) {
                validate = true;
            }
        }

        return validate;
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission: mPerms) {
                if (!isPermissionGranted(permission)) {
                    EasyPermissions.requestPermissions(this, getString(R.string.camera_rationale),
                            PERMISSION_REQUEST_CODE, mPerms);
                    return;
                }
            }
        } else {
            prepareMicrophonePermission();
        }



    }

    private boolean isPermissionGranted(String permission) {
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (CommonUtils.getTargetVersion(this) >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = EasyPermissions.hasPermissions(this, permission);
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(this, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    private void prepareMicrophonePermission() {
        int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
        int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
        int RECORDER_SAMPLE_RATE = 8000;
        int RECORDER_SOURCE = MediaRecorder.AudioSource.MIC;
        // 计算缓冲区大小
        int bufferSize = AudioRecord.getMinBufferSize(
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING);
        bufferSize = Math.max(bufferSize, RECORDER_SAMPLE_RATE * 2);

        // 初始化AudioRecorder
        AudioRecord audioRecorder = new AudioRecord(
                RECORDER_SOURCE,
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                bufferSize);
        // 启动录音
        audioRecorder.startRecording();

        audioRecorder.stop();
        audioRecorder.release();
    }
}
