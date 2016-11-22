package com.cafe.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cafe.R;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.LoginContract;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;


public class LoginActivity extends MVPActivity<LoginContract.View,
        LoginContract.Presenter> implements LoginContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView userImage = (ImageView) findViewById(R.id.user_name).findViewById(R.id.editor_icon);
        userImage.setImageResource(R.mipmap.icon_people);
        ImageView passwordImage = (ImageView) findViewById(R.id.user_password).findViewById(R.id.editor_icon);
        passwordImage.setVisibility(View.GONE);

        EditText passwordEdit = (EditText) findViewById(R.id.user_password).findViewById(R.id.editor_content);
        passwordEdit.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);

        TextInputLayout userName = (TextInputLayout) findViewById(R.id.user_name).findViewById(R.id.editor_layout);
        userName.setHint(getString(R.string.user_name));
        TextInputLayout passwordText = (TextInputLayout) findViewById(R.id.user_password).findViewById(R.id.editor_layout);
        passwordText.setHint(getString(R.string.password));
    }

    @Override
    public LoginContract.Presenter initPresenter() {
        return null;
    }

    public void login(View v) {

    }

    @Override
    public void loginDone(boolean success) {

    }
}
