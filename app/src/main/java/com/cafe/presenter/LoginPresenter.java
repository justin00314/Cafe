package com.cafe.presenter;

import com.cafe.common.mvp.MVPPresenter;
import com.cafe.contract.LoginContract;

/**
 * Created by Rocky on 2016/11/11.
 */

public class LoginPresenter extends MVPPresenter<LoginContract.View, LoginContract.Model>
        implements LoginContract.Presenter<LoginContract.View, LoginContract.Model> {

    @Override
    public LoginContract.Model initModel() {
        return null;
    }

    @Override
    public void login() {

    }
}
