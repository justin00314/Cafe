package com.cafe.model.meeting;

import android.content.Context;

import com.cafe.common.net.HttpManager;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.UrlName;
import com.cafe.contract.MeetingSearchContract;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.QueryMeetingResponse;

/**
 * Created by Rocky on 2016/11/30.
 */

public class BDKMeetingSearchBiz implements MeetingSearchContract.Model {

    private Context mContext;

    public BDKMeetingSearchBiz(Context context) {
        mContext = context;
    }

    @Override
    public void search(int meetingId, JsonHttpResponseHandler<QueryMeetingResponse> handler) {
        MeetingInfo info = new MeetingInfo();
        info.id = meetingId;
        HttpManager.postJson(mContext, UrlName.MEETING_INFO.getUrl(), info, handler);
    }
}
