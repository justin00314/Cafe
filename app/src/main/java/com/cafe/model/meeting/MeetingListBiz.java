package com.cafe.model.meeting;

import android.content.Context;

import com.cafe.common.PreManager;
import com.cafe.common.net.HttpManager;
import com.cafe.common.net.UrlName;
import com.cafe.contract.MeetingListContract;
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

	}
}
