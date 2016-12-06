package com.cafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafe.R;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.LoginContract;
import com.cafe.data.account.LoginRequest;
import com.cafe.presenter.LoginPresenter;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;


public class LoginActivity extends MVPActivity<LoginContract.View,
        LoginContract.Presenter> implements LoginContract.View {

    private EditText mUserName;
    private EditText mPassword;


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
    public void loginDone(boolean success, String errorMessage) {
        if (success) {
            Intent intent = new Intent(this, MeetingListActivity.class);
            startActivity(intent);
	        // 登录成功之后结束自己
	        finish();
        } else {
            Snackbar.make(findViewById(R.id.container_layout), errorMessage, Snackbar.LENGTH_SHORT).show();
        }
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


}
