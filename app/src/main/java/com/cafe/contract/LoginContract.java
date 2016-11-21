package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.common.mvp.base.IMVPPresent;

/**
 * Created by Rocky on 2016/11/11.
 */

public interface LoginContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void loginDone(boolean success);
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends LoginContract.View, M extends LoginContract.Model> extends IMVPPresent<V, M> {
        void login();
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void login();
    }
}
