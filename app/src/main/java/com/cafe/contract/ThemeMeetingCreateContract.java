package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.common.mvp.base.IMVPPresent;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.data.meeting.CreateMeetingRequest;
import com.cafe.data.meeting.CreateMeetingResponse;

/**
 * Created by Rocky on 2016/11/10.
 */

public interface ThemeMeetingCreateContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void submitDone(boolean success, long meetingId);
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends ThemeMeetingCreateContract.View, M extends ThemeMeetingCreateContract.Model> extends IMVPPresent<V, M> {
        void createNewMeeting(CreateMeetingRequest request);
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void createNewMeeting(CreateMeetingRequest request, JsonHttpResponseHandler<CreateMeetingResponse> handler);
    }
}
