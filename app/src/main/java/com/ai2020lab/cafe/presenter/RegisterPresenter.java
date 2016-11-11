package com.ai2020lab.cafe.presenter;

import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.RegisterContract;

/**
 * Created by Rocky on 2016/11/11.
 */

public class RegisterPresenter extends MVPPresenter<RegisterContract.View, RegisterContract.Model>
        implements RegisterContract.Presenter<RegisterContract.View, RegisterContract.Model> {
    @Override
    public void register() {

    }

    @Override
    public RegisterContract.Model initModel() {
        return null;
    }
}
