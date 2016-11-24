package com.cafe.model.meeting;

import android.content.Context;

import com.cafe.common.net.HttpManager;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.UrlName;
import com.cafe.contract.ThemeMeetingCreateContract;
import com.cafe.data.meeting.CreateMeetingRequest;
import com.cafe.data.meeting.CreateMeetingResponse;

/**
 * Created by Rocky on 2016/11/24.
 */

public class BDKMeetingCreateBiz implements ThemeMeetingCreateContract.Model {

    private Context mContext;

    public BDKMeetingCreateBiz(Context context) {
        mContext = context;
    }

    @Override
    public void createNewMeeting(CreateMeetingRequest request, JsonHttpResponseHandler<CreateMeetingResponse> handler) {
        HttpManager.postJson(mContext, UrlName.MEETING_CREATE.getUrl(), request, handler);
    }
}
