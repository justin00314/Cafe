package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.common.mvp.base.IMVPPresent;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.data.account.LoginRequest;
import com.cafe.data.account.LoginResponse;
import com.cafe.data.account.RegisterRequest;
import com.cafe.data.base.ResultResponse;

/**
 * Created by Rocky on 2016/11/11.
 */

public interface LoginContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void loginDone(boolean success, String errorMessage);
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends LoginContract.View, M extends LoginContract.Model> extends IMVPPresent<V, M> {
        void login(LoginRequest request);
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void login(LoginRequest request, JsonHttpResponseHandler<LoginResponse> handler);
        void processToken(String token);
    }
}
