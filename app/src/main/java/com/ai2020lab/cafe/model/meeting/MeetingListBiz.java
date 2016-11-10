package com.ai2020lab.cafe.model.meeting;

import android.content.Context;

import com.ai2020lab.cafe.common.net.HttpManager;
import com.ai2020lab.cafe.common.net.UrlName;
import com.ai2020lab.cafe.contract.MeetingListContract;
import com.ai2020lab.cafe.data.meeting.MeetingListRequest;
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
		data.userID = "1231";
		HttpManager.postJson(context, UrlName.MEETING_LIST.getUrl(), data,
				response);
	}
}
