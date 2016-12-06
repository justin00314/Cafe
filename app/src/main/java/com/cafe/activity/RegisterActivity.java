package com.cafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cafe.R;
import com.cafe.common.TextInputValidater;
import com.cafe.common.camera.HeadOnlyCameraPreview;
import com.cafe.common.camera.SavingHeadPhotoTask;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.LoginContract;
import com.cafe.contract.RegisterContract;
import com.cafe.data.account.LoginRequest;
import com.cafe.data.account.RegisterRequest;
import com.cafe.model.login.BDKLoginBiz;
import com.cafe.presenter.LoginPresenter;
import com.cafe.presenter.RegisterPresenter;

import org.justin.media.CameraManager;
import org.justin.media.interfaces.PhotoTakenCallback;
import org.justin.utils.common.LogUtils;
import org.justin.utils.storage.FileUtils;
import org.justin.utils.system.DisplayUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class RegisterActivity extends MVPActivity<RegisterContract.View,
        RegisterContract.Presenter> implements RegisterContract.View, LoginContract.View, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "RegisterActivity";
    public static final int PERMISSION_CAMERA_REQUEST_CODE = 999;

    private EditText mName;
    private EditText mId;
    private EditText mPassword;
    private EditText mRepassword;

    private HeadOnlyCameraPreview mPreview;
    private File photoFile;
    private boolean canTakePhoto;

    private float mHeadRadius;
    private float mHeadX, mHeadY;
    private int mPreviewViewTopMargin;
    private int mPreviewViewLeftMargin;
    private ImageView mHeadImage;

    private TextInputValidater mTextValidater;
    private LoginContract.Presenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupUI();
        setupLayout();
        setupMVP();
    }


    private void setupUI() {
        supportToolbar(false);

        ImageView userImage = (ImageView) findViewById(R.id.user_name).findViewById(R.id.editor_icon);
        userImage.setImageResource(R.mipmap.icon_people);
        ImageView idImage = (ImageView) findViewById(R.id.user_id).findViewById(R.id.editor_icon);
        idImage.setImageResource(R.mipmap.icon_id);
        ImageView passwordImage = (ImageView) findViewById(R.id.user_password).findViewById(R.id.editor_icon);
        passwordImage.setVisibility(View.GONE);
        ImageView repasswordImage = (ImageView) findViewById(R.id.user_repassword).findViewById(R.id.editor_icon);
        repasswordImage.setVisibility(View.GONE);

        mName = (EditText) findViewById(R.id.user_name).findViewById(R.id.editor_content);
        mId = (EditText) findViewById(R.id.user_id).findViewById(R.id.editor_content);
        mId.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mPassword = (EditText) findViewById(R.id.user_password).findViewById(R.id.editor_content);
        mPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
        mRepassword = (EditText) findViewById(R.id.user_repassword).findViewById(R.id.editor_content);
        mRepassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);

        mRepassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    register(null);
                    return true;
                }

                return false;
            }
        });

        TextInputLayout userName = (TextInputLayout) findViewById(R.id.user_name).findViewById(R.id.editor_layout);
        userName.setHint(getString(R.string.user_name));
        TextInputLayout passwordText = (TextInputLayout) findViewById(R.id.user_password).findViewById(R.id.editor_layout);
        passwordText.setHint(getString(R.string.password));
        TextInputLayout userId = (TextInputLayout) findViewById(R.id.user_id).findViewById(R.id.editor_layout);
        userId.setHint(getString(R.string.user_id));
        TextInputLayout repasswordText = (TextInputLayout) findViewById(R.id.user_repassword).findViewById(R.id.editor_layout);
        repasswordText.setHint(getString(R.string.confirm_password));

        mTextValidater = new TextInputValidater();
        mTextValidater.putValidateItem(mName, getString(R.string.name_not_empty));
        mTextValidater.putValidateItem(mId, getString(R.string.id_not_empty));
        mTextValidater.putValidateItem(mPassword, getString(R.string.password_not_empty));
        mTextValidater.putValidateItem(mRepassword, getString(R.string.confirm_password_not_empty));
    }

    private void setupLayout() {

        setupCameraLayout();
        setupEditDistrictLayout();
    }

    private void setupCameraLayout() {
        ViewGroup preview = (ViewGroup) findViewById(R.id.camera_preview_container);

        int screenHeight = DisplayUtils.getScreenHeight(this);
        int screenWidth = DisplayUtils.getScreenWidth(this);

        float previewRatio = 3f / 4f; // width:height
        float screenRatio = (float) screenWidth / (float) screenHeight;

        int previewHeight = screenHeight;
        int previewWidth = (int) (screenWidth * 0.7);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) preview.getLayoutParams();

        if (screenRatio < previewRatio) {
            previewHeight = (int) (previewWidth / previewRatio);
        } else {
            previewWidth = (int) (previewHeight * previewRatio);
        }

        int marginTop = (int) DisplayUtils.dpToPx(this, 50);
        layoutParams.topMargin = marginTop;
        mPreviewViewTopMargin = marginTop;

        int marginLeft = (screenWidth - previewWidth) / 2;
        layoutParams.leftMargin = marginLeft;
        mPreviewViewLeftMargin = marginLeft;

        layoutParams.width = previewWidth;
        layoutParams.height = previewHeight;

        preview.setLayoutParams(layoutParams);

        mHeadRadius = (float) (previewWidth / 3);
        mHeadX = previewWidth / 2;
        mHeadY = mHeadRadius;

        mHeadImage = (ImageView) preview.findViewById(R.id.head_rect);
        RelativeLayout.LayoutParams headLayout = (RelativeLayout.LayoutParams) mHeadImage.getLayoutParams();
        headLayout.width = (int) mHeadRadius * 2;
        headLayout.height = (int) mHeadRadius * 2;
        mHeadImage.setLayoutParams(headLayout);
    }

    private void setupEditDistrictLayout() {
        int screenHeight = DisplayUtils.getScreenHeight(this);
        int screenWidth = DisplayUtils.getScreenWidth(this);

        ViewGroup editDistrict = (ViewGroup) findViewById(R.id.edit_district);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editDistrict.getLayoutParams();

        layoutParams.topMargin = (int) (mHeadY + mHeadRadius + mPreviewViewTopMargin);
        editDistrict.setLayoutParams(layoutParams);
    }

    private void setupMVP() {
        mLoginPresenter = new LoginPresenter(this);
        mLoginPresenter.setModel(new BDKLoginBiz(this));
        mLoginPresenter.attachView(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 检查摄像头权限，android 6.0需要
     */
    private void checkCameraPermission() {
        // 没有授权的情况下询问用户是否授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            String[] perms = {Manifest.permission.CAMERA};
            EasyPermissions.requestPermissions(this, getString(R.string.camera_rationale),
                    PERMISSION_CAMERA_REQUEST_CODE, perms);
        } else {
            getPresenter().loadCamera();
        }
    }

    @AfterPermissionGranted(PERMISSION_CAMERA_REQUEST_CODE)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA};
    if (EasyPermissions.hasPermissions(this, perms)) {
        getPresenter().loadCamera();
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
                    .setRequestCode(PERMISSION_CAMERA_REQUEST_CODE)
                    .build()
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                getPresenter().loadCamera();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkCameraPermission();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // release the camera immediately on pause event
        //	releaseCamera();
    }

    public void register(android.view.View v) {


        if (photoFile == null) {
            Snackbar.make(findViewById(R.id.container_layout), getString(R.string.prompt_need_photo),
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!mTextValidater.validate()) {
            return;
        }

        if (!mPassword.getText().toString().equals(mRepassword.getText().toString())) {
            Toast.makeText(this, R.string.repassword_wrong, Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest();
        request.password = mPassword.getText().toString();
        request.userName = mName.getText().toString();
        request.workNumber = mId.getText().toString();

        getPresenter().register(request, photoFile.getPath());

    }

    /**
     * 自动对焦并拍照
     */
    public void takePhoto(View v) {
        if (canTakePhoto) {

            canTakePhoto = false;

            CameraManager.getInstance().takePicture(new PhotoTakenCallback() {
                @Override
                public void photoTakenSuccess(byte[] data, int orientation) {
                    // 调用异步任务保存相片
                    savePhoto(data, orientation);
                }

                @Override
                public void photoTakenFailure() {
                    photoFile = null;
                    canTakePhoto = true;
                }
            });
        }
    }

    private void savePhoto(byte[] data, int orientation) {
        SavingHeadPhotoTask.ClipRect headRect = getHeadRect();
        SavingHeadPhotoTask savingPhotoTask = new SavingHeadPhotoTask(getActivity(), data,
                getPhotoFileName(), getPhotoPath(),
                orientation, true, headRect, new SavePhotoCallback());
        savingPhotoTask.execute();
    }

    private String getPhotoFileName() {
        return "registerHead" + "." + FileUtils.ExtensionName.JPG;
    }

    private String getPhotoPath() {
        File photoDir = FileUtils.getDiskCacheDir(getActivity(), "headers");
        return photoDir.getPath();
    }

    private SavingHeadPhotoTask.ClipRect getHeadRect() {
        final float x = mPreview.headX;
        final float y = mPreview.headY;
        final float r = mPreview.headRadius;

        final float left = (x - r) / mPreview.getWidth();
        final float top = (y - r) / mPreview.getHeight();
        final float right = (x + r) / mPreview.getWidth();
        final float bottom = (y + r) / mPreview.getHeight();

        SavingHeadPhotoTask.ClipRect rect = new SavingHeadPhotoTask.ClipRect();
        rect.setLeft(left);
        rect.setTop(top);
        rect.setBottom(bottom);
        rect.setRight(right);

        return rect;
    }

    private void releaseCamera() {
//        if (mCamera != null) {
//            // release the camera for other applications
//            mCamera.release();
//            mCamera = null;
//
//            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//            preview.removeView(mPreview);
//        }

        CameraManager.getInstance().stopPreview();
        CameraManager.getInstance().closeDriver();

//		ViewGroup preview = (ViewGroup) findViewById(R.id.camera_preview_container).findViewById(R.id.camera_preview);
//		if (preview != null && mPreview != null)
//			preview.removeView(mPreview);

        canTakePhoto = false;
    }

    @Override
    public RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }


    @Override
    public void registerDone(boolean success, String errorMessage) {
        if (!success) {
            dismissLoadingProgress();
            Snackbar.make(findViewById(R.id.container_layout), errorMessage, Snackbar.LENGTH_SHORT).show();
        } else {
            LoginRequest request = new LoginRequest();
            request.password = mPassword.getText().toString();
            request.userName = mName.getText().toString();
            mLoginPresenter.login(request);
        }
    }

    @Override
    public void initCameraSurface() {
        if (mPreview == null) {
            mPreview = new HeadOnlyCameraPreview(this);

            mPreview.headRadius = mHeadRadius;
            mPreview.headX = mHeadX;
            mPreview.headY = mHeadY;

            mPreview.getHolder().addCallback(new SurfaceCallback());

            ViewGroup preview = (ViewGroup) findViewById(R.id.camera_preview).findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }


    }

    @Override
    public void loadCameraFail() {
        canTakePhoto = false;
        Snackbar.make(findViewById(R.id.container_layout), getString(R.string.prompt_open_camera_failure),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void loginDone(boolean success, String errorMessage) {
        if (success) {
            Intent intent = new Intent(this, MeetingListActivity.class);
            startActivity(intent);
	        // 登录成功之后结束自己
	        finish();
        } else {
            Snackbar.make(findViewById(R.id.container_layout), R.string.login_fail, Snackbar.LENGTH_SHORT).show();
        }
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            CameraManager.getInstance().startPreview(surfaceHolder);
            canTakePhoto = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (holder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            CameraManager.getInstance().stopPreview();

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            CameraManager.getInstance().startPreview(holder);
            canTakePhoto = true;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            // surfaceView销毁的时候停止预览并释放相机资源
            releaseCamera();
        }
    }

    private class SavePhotoCallback implements SavingHeadPhotoTask.HeadPhotoSavedListener {
        @Override
        public void savedBefore() {
            showLoadingProgress(null);
        }

        @Override
        public void savedFailure() {
            dismissLoading();

            Snackbar.make(findViewById(R.id.container_layout), getString(R.string.photo_prompt_saving_failure),
                    Snackbar.LENGTH_SHORT).show();

            photoFile = null;

            canTakePhoto = true;
        }

        @Override
        public void savedSuccess(File photo) {
            dismissLoading();
            LogUtils.i(TAG, "拍照成功，照片路径-->" + photo.getPath());

            if (photo != null && photo.exists()) {
                LogUtils.i(TAG, "拍照成功，照片截图路径-->" + photo.getPath());
                photoFile = photo;

                Bitmap bm = BitmapFactory.decodeFile(photo.getPath());
                mHeadImage.setImageBitmap(bm);
            }

            canTakePhoto = true;

        }
    }
}
