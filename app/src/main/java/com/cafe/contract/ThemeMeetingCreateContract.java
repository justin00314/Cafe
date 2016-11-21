package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.common.mvp.base.IMVPPresent;

/**
 * Created by Rocky on 2016/11/10.
 */

public interface ThemeMeetingCreateContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void submitDone(boolean success);
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends ThemeMeetingCreateContract.View, M extends ThemeMeetingCreateContract.Model> extends IMVPPresent<V, M> {
        void submit();
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void submitNewMeeting();
    }
}
