package com.cafe.model.meeting;

import android.content.Context;

import com.cafe.common.CommonUtils;
import com.cafe.common.net.HttpManager;
import com.cafe.common.net.UrlName;
import com.cafe.contract.ThemeDetailContract;
import com.cafe.data.account.LogUserRequest;
import com.cafe.data.meeting.GetNowTalkerRequest;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.ProcedureListRequest;
import com.cafe.data.meeting.StartEpisodeRequest;
import com.cafe.data.meeting.StartThemeRequest;
import com.cafe.data.meeting.StopEpisodeRequest;
import com.cafe.data.meeting.StopThemeRequest;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 主题会议详情Model实现类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class ThemeDetailBiz implements ThemeDetailContract.Model {

	private Context context;

	public ThemeDetailBiz(Context context) {
		this.context = context;
	}

	@Override
	public void getNowTalker(MeetingUserInfo info, ResponseHandlerInterface response) {
		GetNowTalkerRequest data = new GetNowTalkerRequest();
		data.id = info.id;
		HttpManager.postJson(context, UrlName.PROCEDURE_TALKER.getUrl(), data, response);
	}

	@Override
	public void loadProcedureList(MeetingUserInfo info, ResponseHandlerInterface response) {
		ProcedureListRequest data = new ProcedureListRequest();
		data.id = info.id;
		HttpManager.postJson(context, UrlName.PROCEDURE_LIST.getUrl(), data, response);
	}

	@Override
	public void startTheme(MeetingUserInfo info, ResponseHandlerInterface response) {
		StartThemeRequest data = new StartThemeRequest();
		data.id = info.id;
		data.time = CommonUtils.getCurrentTime();
		HttpManager.postJson(context, UrlName.PROCEDURE_START_TOPIC.getUrl(), data, response);

	}

	@Override
	public void stopTheme(MeetingUserInfo info, ResponseHandlerInterface response) {
		StopThemeRequest data = new StopThemeRequest();
		data.id = info.id;
		data.time = CommonUtils.getCurrentTime();
		HttpManager.postJson(context, UrlName.PROCEDURE_STOP_TOPIC.getUrl(), data, response);
	}

	@Override
	public void startEpisode(MeetingUserInfo info, ResponseHandlerInterface response) {
		StartEpisodeRequest data = new StartEpisodeRequest();
		data.id = info.id;
		data.time = CommonUtils.getCurrentTime();
		HttpManager.postJson(context, UrlName.PROCEDURE_START_EPISODE.getUrl(), data, response);
	}

	@Override
	public void stopEpisode(MeetingUserInfo info, ResponseHandlerInterface response) {
		StopEpisodeRequest data = new StopEpisodeRequest();
		data.id = info.id;
		data.time = CommonUtils.getCurrentTime();
		HttpManager.postJson(context, UrlName.PROCEDURE_STOP_EPISODE.getUrl(), data, response);
	}
}
