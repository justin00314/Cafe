package com.cafe.model.meeting;

import android.content.Context;
import android.graphics.Paint;

import com.cafe.common.PreManager;
import com.cafe.common.net.HttpManager;
import com.cafe.common.net.UrlName;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.DeviceType;
import com.cafe.data.account.LogoutRequest;
import com.cafe.data.meeting.JoinMeetingRequest;
import com.cafe.data.meeting.MeetingListRequest;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 会议列表界面Model实现类
 * Created by Justin Z on 2016/11/10.
 * 502953057@qq.com
 */

public class MeetingListBiz implements MeetingListContract.Model {

	private Context context;

	public MeetingListBiz(Context context){
		this.context = context;
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
	public void joinMeeting(ResponseHandlerInterface response) {
		JoinMeetingRequest data = new JoinMeetingRequest();

	}

	/**
	 * 退出会议
	 */
	@Override
	public void quitMeeting(ResponseHandlerInterface response) {

	}

	/**
	 * 取消会议
	 */
	@Override
	public void cancelMeeting(ResponseHandlerInterface response) {

	}

	/**
	 * 解散会议
	 */
	@Override
	public void dismissMeeting(ResponseHandlerInterface response) {

	}

	/**
	 * 请求创建头脑风暴
	 */
	@Override
	public void requestBrainStorm(ResponseHandlerInterface response) {

	}
}
