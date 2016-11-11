package com.ai2020lab.cafe.contract;

import com.ai2020lab.cafe.common.mvp.base.BaseModel;
import com.ai2020lab.cafe.common.mvp.base.BaseView;
import com.ai2020lab.cafe.common.mvp.base.IMVPPresent;

/**
 * Created by Rocky on 2016/11/11.
 */

public interface MeetingSearchContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void showSearchResults();
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends MeetingSearchContract.View, M extends MeetingSearchContract.Model> extends IMVPPresent<V, M> {
        void search(String meetingId);
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void search(String meetingId);
    }
}
