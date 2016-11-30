package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.common.mvp.base.IMVPPresent;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.data.account.LoginResponse;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingListResponse;
import com.cafe.data.meeting.MeetingRoomListResponse;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.QueryMeetingResponse;

import java.util.List;

/**
 * Created by Rocky on 2016/11/11.
 */

public interface MeetingSearchContract {

    /**
     * View的接口方法，由Activity去实现
     */
    interface View extends BaseView {
        void showSearchResults(boolean result, List<MeetingInfo> meetings);
    }

    /**
     * Presenter接口方法，一般是Activity中的业务逻辑方法
     */
    interface Presenter<V extends MeetingSearchContract.View, M extends MeetingSearchContract.Model> extends IMVPPresent<V, M> {
        void search(int meetingId);
    }

    /**
     * Model接口方法，一般是数据操作方法,不一定存在Model
     */
    interface Model extends BaseModel {
        void search(int meetingId, JsonHttpResponseHandler<QueryMeetingResponse> handler);
    }
}
