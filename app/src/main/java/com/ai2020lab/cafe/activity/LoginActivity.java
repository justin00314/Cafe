package com.ai2020lab.cafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.common.mvp.MVPActivity;
import com.ai2020lab.cafe.contract.LoginContract;

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
        passwordImage.setImageResource(R.mipmap.icon_key);

        EditText passwordEdit = (EditText) findViewById(R.id.user_password).findViewById(R.id.editor_content);
        passwordEdit.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);

        TextView userName = (TextView) findViewById(R.id.user_name).findViewById(R.id.editor_title);
        userName.setText(R.string.user_name);
        TextView passwordText = (TextView) findViewById(R.id.user_password).findViewById(R.id.editor_title);
        passwordText.setText(R.string.password);
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
