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
import com.cafe.data.meeting.StartEpisodeRequest;
import com.cafe.data.meeting.StartEpisodeResponse;
import com.cafe.data.meeting.StartThemeResponse;
import com.cafe.data.meeting.StopEpisodeResponse;
import com.cafe.data.meeting.StopThemeResponse;
import com.cafe.model.meeting.ThemeDetailBiz;
import com.loopj.android.http.ResponseHandlerInterface;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ToastUtils;

import java.io.IOException;
import java.net.URI;

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
		if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		view.setNowTalker(jsonObj.data);
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
		if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		if (!TextUtils.isEmpty(jsonObj.data.filterTime)) {
			PreManager.setProcedureFilterTime(context, jsonObj.data.filterTime);
		}
		if (jsonObj.data.procedureInfos != null || jsonObj.data.procedureInfos.size() != 0) {
			// 界面加载说话详情列表
			view.loadProcedureList(jsonObj.data.procedureInfos);
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
		if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
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
		if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
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
		if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		if (jsonObj.data.result) {
			PreManager.setStartEpisodeFlag(context, true);
			ToastUtils.getInstance().showToast(context, R.string.prompt_start_episode_success);
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
		if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		if (jsonObj.data.result) {
			PreManager.setStartEpisodeFlag(context, false);
			ToastUtils.getInstance().showToast(context, R.string.prompt_stop_episode_success);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_stop_episode_failure);
		}
	}

}
