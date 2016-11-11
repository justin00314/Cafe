package com.ai2020lab.cafe.contract;

import com.ai2020lab.cafe.common.mvp.base.BaseModel;
import com.ai2020lab.cafe.common.mvp.base.BaseView;
import com.ai2020lab.cafe.common.mvp.base.IMVPPresent;

/**
 * Created by Rocky on 2016/11/11.
 */

public interface RegisterContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void registerDone(boolean success);
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends RegisterContract.View, M extends RegisterContract.Model> extends IMVPPresent<V, M> {
        void register();
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void register();
    }
}
