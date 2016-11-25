package com.cafe.model.meeting;

import android.content.Context;

import com.cafe.common.PreManager;
import com.cafe.common.net.HttpManager;
import com.cafe.common.net.UrlName;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.DeviceType;
import com.cafe.data.account.LogUserRequest;
import com.cafe.data.account.LogoutRequest;
import com.cafe.data.meeting.CancelMeetingRequest;
import com.cafe.data.meeting.DismissMeetingRequest;
import com.cafe.data.meeting.JoinMeetingRequest;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingListRequest;
import com.cafe.data.meeting.QuitMeetingRequest;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 会议列表界面Model实现类
 * Created by Justin Z on 2016/11/10.
 * 502953057@qq.com
 */

public class MeetingListBiz implements MeetingListContract.Model {

	private Context context;

	public MeetingListBiz(Context context) {
		this.context = context;
	}

	/**
	 * 获取登录人用户信息
	 */
	@Override
	public void getUserInfo(ResponseHandlerInterface response) {
		LogUserRequest data = new LogUserRequest();
		HttpManager.postJson(context, UrlName.USER_INFO.getUrl(), data, response);
	}

	/**
	 * 加载会议列表网络数据
	 */
	@Override
	public void loadMeetingList(ResponseHandlerInterface response) {
		MeetingListRequest data = new MeetingListRequest();
		data.filterTime = PreManager.getMeetingListFilterTime(context);
		HttpManager.postJson(context, UrlName.MEETING_LIST.getUrl(), data,
				response);
	}

	/**
	 * 用户登出
	 */
	@Override
	public void logout(ResponseHandlerInterface response) {
		LogoutRequest data = new LogoutRequest();
		data.deviceType = DeviceType.MOBILE;
		HttpManager.postJson(context, UrlName.USER_LOGOUT.getUrl(), data,
				response);
	}

	/**
	 * 加入会议
	 */
	@Override
	public void joinMeeting(MeetingInfo info, ResponseHandlerInterface response) {
		JoinMeetingRequest data = new JoinMeetingRequest();
		data.id = info.id;
		HttpManager.postJson(context, UrlName.MEETING_JOIN.getUrl(), data,
				response);
	}

	/**
	 * 退出会议
	 */
	@Override
	public void quitMeeting(MeetingInfo info, ResponseHandlerInterface response) {
		QuitMeetingRequest data = new QuitMeetingRequest();
		data.id = info.id;
		HttpManager.postJson(context, UrlName.MEETING_QUIT.getUrl(), data,
				response);
	}

	/**
	 * 取消会议
	 */
	@Override
	public void cancelMeeting(MeetingInfo info, ResponseHandlerInterface response) {
		CancelMeetingRequest data = new CancelMeetingRequest();
		data.id = info.id;
		HttpManager.postJson(context, UrlName.MEETING_CANCEL.getUrl(), data,
				response);
	}

	/**
	 * 解散会议
	 */
	@Override
	public void dismissMeeting(MeetingInfo info, ResponseHandlerInterface response) {
		DismissMeetingRequest data = new DismissMeetingRequest();
		data.id = info.id;
		HttpManager.postJson(context, UrlName.MEETING_DISMISS.getUrl(), data,
				response);
	}

	/**
	 * 请求创建头脑风暴
	 */
	@Override
	public void requestBrainStorm(ResponseHandlerInterface response) {

	}
}
