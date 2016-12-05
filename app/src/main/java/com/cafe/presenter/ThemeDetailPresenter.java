package com.cafe.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.cafe.R;
import com.cafe.common.PreManager;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.ResultCode;
import com.cafe.contract.ThemeDetailContract;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.meeting.GetNowTalkerResponse;
import com.cafe.data.meeting.MeetingListResponse;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.ProcedureListRequest;
import com.cafe.data.meeting.ProcedureListResponse;
import com.cafe.data.meeting.QueryUnstopEventResponse;
import com.cafe.data.meeting.SpeakType;
import com.cafe.data.meeting.StartEpisodeRequest;
import com.cafe.data.meeting.StartEpisodeResponse;
import com.cafe.data.meeting.StartThemeResponse;
import com.cafe.data.meeting.StopEpisodeResponse;
import com.cafe.data.meeting.StopThemeResponse;
import com.cafe.model.meeting.ThemeDetailBiz;
import com.loopj.android.http.ResponseHandlerInterface;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.TimeUtils;
import org.justin.utils.common.ToastUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * 主题会议详情Presenter
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class ThemeDetailPresenter extends MVPPresenter<ThemeDetailContract.View,
		ThemeDetailContract.Model> implements ThemeDetailContract.Presenter {

	private final static String TAG = ThemeDetailPresenter.class.getSimpleName();

	private Context context;

	public ThemeDetailPresenter(Context context) {
		this.context = context;
		setModel(initModel());
	}


	@Override
	public ThemeDetailContract.Model initModel() {
		return new ThemeDetailBiz(context);
	}

	/**
	 * 收到用户在会广播后处理
	 */
	@Override
	public void doReceiveAtMeeting(MeetingUserInfo info) {
		// 会议结束的话就退出界面
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if (info.id != -1) return;
		view.finishActivity();
	}

	/**
	 * 开始会议计时
	 */
	@Override
	public void startMeetingTime(MeetingUserInfo info) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		// TODO:根据当前时间和会议开始时间计算计时器的初始值
		long startTime = TimeUtils.dateToTimeStamp(info.startTime,
				TimeUtils.Template.YMDHMS) / 1000;
		long currentTime = new Date().getTime() / 1000;
		LogUtils.i(TAG, "会议开始时间-->" + startTime);
		LogUtils.i(TAG, "当前时间-->" + currentTime);
		long base = currentTime - startTime;
		view.startMeetingTime(base);
	}

	/**
	 * 结束会议计时
	 */
	@Override
	public void stopMeetingTime() {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		view.stopMeetingTime();
	}

	/**
	 * 获取当前说话人
	 */
	@Override
	public void getNowTalker(final MeetingUserInfo info) {
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		themeDetailBiz.getNowTalker(info, new JsonHttpResponseHandler<GetNowTalkerResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, GetNowTalkerResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				// TODO:不做任何处理

			}

			@Override
			public void onHandleFailure(String errorMsg) {
				// TODO:不做任何处理
			}
		});

	}

	/**
	 * 获取当前说话人成功处理
	 */
	private void handleSuccess(GetNowTalkerResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if(TextUtils.isEmpty(jsonObj.data.userName)) return;
		view.setNowTalker(jsonObj.data);
		// TODO:针对插话人退出了应用又重新进来的情况
		// 到计时重新开始，暂时没有返回退出时候的时间
		if (jsonObj.data.speakType == SpeakType.EPISODE && jsonObj.data.isSelfFlag &&
				view.isTimeDescStart()) {
			view.refreshAfterStartEpisode();
		}
	}

	/**
	 * 加载说话记录列表
	 */
	@Override
	public void loadProcedureList(final MeetingUserInfo info) {
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		themeDetailBiz.loadProcedureList(info, new JsonHttpResponseHandler<ProcedureListResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, ProcedureListResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				// TODO:不做任何处理

			}

			@Override
			public void onHandleFailure(String errorMsg) {
				// TODO:不做任何处理
			}
		});

	}

	private void handleSuccess(ProcedureListResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if (!TextUtils.isEmpty(jsonObj.data.filterTime)) {
			PreManager.setProcedureFilterTime(context, jsonObj.data.filterTime);
		}
		if (jsonObj.data.procedureInfos != null || jsonObj.data.procedureInfos.size() != 0) {
			// 界面加载说话详情列表
			view.loadProcedureList(jsonObj.data.procedureInfos);
		}
	}

	/**
	 * 操作开始或者结束主题
	 */
	@Override
	public void operateTheme(final MeetingUserInfo info) {
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		ToastUtils.getInstance().showToast(context, R.string.prompt_query_unstop_event);
		themeDetailBiz.queryUnstopEvent(info, new JsonHttpResponseHandler<QueryUnstopEventResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers,
			                            QueryUnstopEventResponse jsonObj) {
				handleThemeSuccess(info, jsonObj);
			}

			@Override
			public void onCancel() {
				ToastUtils.getInstance().showToast(context,
						R.string.prompt_query_unstop_event_failure);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
				ToastUtils.getInstance().showToast(context,
						R.string.prompt_query_unstop_event_failure);
			}
		});
	}

	private void handleThemeSuccess(MeetingUserInfo info, QueryUnstopEventResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		// 用户在事件中
		if (jsonObj.data.result) {
			if (jsonObj.data.speakType == SpeakType.THEME) {
				// 已经开始主题则结束主题
				stopTheme(info);
			} else if (jsonObj.data.speakType == SpeakType.EPISODE) {
				ToastUtils.getInstance().showToast(context,
						R.string.prompt_episode_);
			}
		} else {
			// 不在事件中则开始主题
			startTheme(info);
		}
	}

	/**
	 * 操作开始或者结束插话
	 */
	@Override
	public void operateEpisode(final MeetingUserInfo info) {
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		ToastUtils.getInstance().showToast(context, R.string.prompt_query_unstop_event);
		themeDetailBiz.queryUnstopEvent(info, new JsonHttpResponseHandler<QueryUnstopEventResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers,
			                            QueryUnstopEventResponse jsonObj) {
				handleEpisodeSuccess(info, jsonObj);
			}

			@Override
			public void onCancel() {
				ToastUtils.getInstance().showToast(context,
						R.string.prompt_query_unstop_event_failure);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
				ToastUtils.getInstance().showToast(context,
						R.string.prompt_query_unstop_event_failure);
			}
		});
	}

	private void handleEpisodeSuccess(MeetingUserInfo info, QueryUnstopEventResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		// 用户在事件中
		if (jsonObj.data.result) {
			if (jsonObj.data.speakType == SpeakType.EPISODE) {
				// 已经开始主题则结束主题
				stopEpisode(info);
			} else if (jsonObj.data.speakType == SpeakType.THEME) {
				ToastUtils.getInstance().showToast(context,
						R.string.prompt_theme_);
			}
		} else {
			// 不在事件中则开始主题
			startEpisode(info);
		}
	}


	/**
	 * 开始主题
	 */
	@Override
	public void startTheme(MeetingUserInfo info) {
		LogUtils.i(TAG, "-->开始主题请求");
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		ToastUtils.getInstance().showToast(context, R.string.prompt_start_theme);
		themeDetailBiz.startTheme(info, new JsonHttpResponseHandler<StartThemeResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, StartThemeResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				ToastUtils.getInstance().showToast(context, R.string.prompt_start_theme_failure);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
				ToastUtils.getInstance().showToast(context, R.string.prompt_start_theme_failure);
			}
		});
	}

	private void handleSuccess(StartThemeResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if (jsonObj.data.result) {
			PreManager.setStartTopicFlag(context, true);
			ToastUtils.getInstance().showToast(context, R.string.prompt_start_theme_success);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_start_theme_failure);
		}
	}

	/**
	 * 结束主题
	 */
	@Override
	public void stopTheme(MeetingUserInfo info) {
		LogUtils.i(TAG, "-->结束主题请求");
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		ToastUtils.getInstance().showToast(context, R.string.prompt_stop_theme);
		themeDetailBiz.stopTheme(info, new JsonHttpResponseHandler<StopThemeResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, StopThemeResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				ToastUtils.getInstance().showToast(context, R.string.prompt_stop_theme_failure);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
				ToastUtils.getInstance().showToast(context, R.string.prompt_stop_theme_failure);
			}
		});
	}

	private void handleSuccess(StopThemeResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if (jsonObj.data.result) {
			PreManager.setStartTopicFlag(context, false);
			ToastUtils.getInstance().showToast(context, R.string.prompt_stop_theme_success);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_stop_theme_failure);
		}
	}

	/**
	 * 开始插话
	 */
	@Override
	public void startEpisode(MeetingUserInfo info) {
		LogUtils.i(TAG, "-->开始插话请求");
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		ToastUtils.getInstance().showToast(context, R.string.prompt_start_episode);
		themeDetailBiz.startEpisode(info, new JsonHttpResponseHandler<StartEpisodeResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, StartEpisodeResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				ToastUtils.getInstance().showToast(context, R.string.prompt_start_episode_failure);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
				ToastUtils.getInstance().showToast(context, R.string.prompt_start_episode_failure);
			}
		});
	}

	private void handleSuccess(StartEpisodeResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if (jsonObj.data.result) {
			PreManager.setStartEpisodeFlag(context, true);
			ToastUtils.getInstance().showToast(context, R.string.prompt_start_episode_success);
			view.refreshAfterStartEpisode();
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_start_episode_failure);
		}
	}

	/**
	 * 结束插话
	 */
	@Override
	public void stopEpisode(MeetingUserInfo info) {
		LogUtils.i(TAG, "-->结束插话请求");
		ThemeDetailContract.Model themeDetailBiz = getModel();
		if (themeDetailBiz == null) return;
		ToastUtils.getInstance().showToast(context, R.string.prompt_stop_episode);
		themeDetailBiz.stopEpisode(info, new JsonHttpResponseHandler<StopEpisodeResponse>() {

			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, StopEpisodeResponse jsonObj) {
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
				ToastUtils.getInstance().showToast(context, R.string.prompt_stop_episode_failure);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
				ToastUtils.getInstance().showToast(context, R.string.prompt_stop_episode_failure);
			}
		});
	}

	private void handleSuccess(StopEpisodeResponse jsonObj) {
		ThemeDetailContract.View view = getView();
		if (view == null) return;
		if (jsonObj.data.result) {
			PreManager.setStartEpisodeFlag(context, false);
			ToastUtils.getInstance().showToast(context, R.string.prompt_stop_episode_success);
			view.refreshAfterStopEpisode();
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_stop_episode_failure);
		}
	}

}
