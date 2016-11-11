package com.ai2020lab.cafe.presenter;

import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.LoginContract;

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
