package com.cafe.model.meeting;

import android.content.Context;

import com.cafe.common.CommonUtils;
import com.cafe.common.net.HttpManager;
import com.cafe.common.net.UrlName;
import com.cafe.contract.BrainStormDetailContract;
import com.cafe.data.account.UserInfo;
import com.cafe.data.meeting.BrainStormDismissRequest;
import com.cafe.data.meeting.MeetingParticipantListRequest;
import com.cafe.data.meeting.MeetingUserInfo;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 临时会议详情Model实现类
 * Created by Justin Z on 2016/12/7.
 * 502953057@qq.com
 */

public class BrainStormDetailBiz implements BrainStormDetailContract.Model {

	private final static String TAG = BrainStormDetailBiz.class.getSimpleName();

	private Context context;

	public BrainStormDetailBiz(Context context) {
		this.context = context;
	}

	/**
	 * 查询会议参与人列表
	 */
	@Override
	public void queryMeetingParticipantList(MeetingUserInfo info,
	                                        ResponseHandlerInterface response) {
		MeetingParticipantListRequest data = new MeetingParticipantListRequest();
		data.id = info.id;
		// TODO:暂时不做分页
		data.pageNumber = 0;
		data.pageSize = 100;
		HttpManager.postJson(context, UrlName.MEETING_PARTICIPANT_LIST.getUrl(), data, response);
	}

	/**
	 * 请求解散临时会议，发送请求成功并不代表会议已经解散
	 */
	@Override
	public void requestForDismissMeeting(MeetingUserInfo info, ResponseHandlerInterface response) {
		BrainStormDismissRequest data = new BrainStormDismissRequest();
		data.time = CommonUtils.getCurrentTime();
		data.id = info.id;
		HttpManager.postJson(context, UrlName.MEETING_DISMISS_BRAIN_STORM.getUrl(), data,
				response);
	}

	/**
	 * 查询用户是否还在某个会议
	 */
	@Override
	public void getIsAtSomeMeeting(ResponseHandlerInterface response) {
		UserInfo data = new UserInfo();
		HttpManager.postJson(context, UrlName.PRESENT_AT_MEETING.getUrl(), data, response);
	}


}
