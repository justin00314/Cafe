package com.cafe.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.cafe.R;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.contract.BrainStormDetailContract;
import com.cafe.data.meeting.BrainStormCreateResponse;
import com.cafe.data.meeting.MeetingType;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.QueryMeetingUserResponse;
import com.cafe.model.meeting.BrainStormDetailBiz;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.TimeUtils;
import org.justin.utils.common.ToastUtils;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * 头脑风暴详情 Presenter
 * Created by Justin Z on 2016/12/7.
 * 502953057@qq.com
 */

public class BrainStormDetailPresenter extends MVPPresenter<BrainStormDetailContract.View,
		BrainStormDetailContract.Model> implements BrainStormDetailContract.Presenter {

	private final static String TAG = BrainStormDetailPresenter.class.getSimpleName();

	private Context context;

	public BrainStormDetailPresenter(Context context) {
		this.context = context;
		setModel(initModel());
	}

	@Override
	public BrainStormDetailContract.Model initModel() {
		return new BrainStormDetailBiz(context);
	}

	/**
	 * 加载会议参与人列表
	 */
	@Override
	public void loadMeetingParticipantList(MeetingUserInfo info) {

	}

	/**
	 * 收到用户是否在会的广播后处理
	 */
	@Override
	public void doReceiveAtMeeting(MeetingUserInfo info) {
		// 会议结束的话就退出界面
		BrainStormDetailContract.View view = getView();
		if (view == null) return;
		if (info.id != -1) return;
		view.finishActivity();
	}

	/**
	 * 开始会议计时
	 */
	@Override
	public void startMeetingTime(MeetingUserInfo info) {
		BrainStormDetailContract.View view = getView();
		if (view == null) return;
		long startTime = TimeUtils.dateToTimeStamp(info.startTime,
				TimeUtils.Template.YMDHMS) / 1000;
		long currentTime = new Date().getTime() / 1000;
		LogUtils.i(TAG, "会议开始时间-->" + startTime);
		LogUtils.i(TAG, "当前时间-->" + currentTime);
		long base = currentTime - startTime;
		view.startMeetingTime(base);
	}

	/**
	 * 停止会议计时
	 */
	@Override
	public void stopMeetingTime() {
		BrainStormDetailContract.View view = getView();
		if (view == null) return;
		view.stopMeetingTime();
	}

	/**
	 * 摇一摇手机解散临时会议
	 */
	@Override
	public void shakePhoneForBrainStorm() {
		BrainStormDetailContract.View view = getView();
		if (view == null) return;
		// TODO:停止监听摇一摇,避免多次摇晃
		if (view.isStartShake()) view.stopShake();
		BrainStormDetailContract.Model brainStormBiz = getModel();
		if (brainStormBiz == null) return;
		brainStormBiz.getIsAtSomeMeeting(
				new JsonHttpResponseHandler<QueryMeetingUserResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            QueryMeetingUserResponse jsonObj) {
						handleSuccess(jsonObj);
					}

					@Override
					public void onCancel() {
						BrainStormDetailContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
					}

					@Override
					public void onHandleFailure(String errorMsg) {
						BrainStormDetailContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_present_meeting_failure);
					}
				});
	}

	private void handleSuccess(QueryMeetingUserResponse jsonObj) {
		BrainStormDetailContract.View view = getView();
		if (view == null) return;
		// 用户不在会才能创建临时会议
		if (jsonObj.data.id == -1) {
			if (!view.isStartShake()) view.startShake();
			ToastUtils.getInstance().showToast(context, R.string.prompt_at_meeting);
		}
		// 用户已经在会
		else {
			// 如果用户已经在头脑风暴会议中则解散会议
			if (jsonObj.data.type == MeetingType.BRAIN_STORM.getId()) {
				LogUtils.i(TAG, "头脑风暴解散-->");
				MeetingUserInfo info = new MeetingUserInfo();
				info.id = jsonObj.data.id;
				dismissBrainStorm(info);
			}
			// 提示用户已经在主题会议中
			else if (jsonObj.data.type == MeetingType.THEME.getId()) {
				LogUtils.i(TAG, "头脑风暴操作提示用户已经在主题会议中-->");
				if (!view.isStartShake()) view.startShake();
				String msg = context.getString(R.string.prompt_present_meeting_);
				if (TextUtils.isEmpty(jsonObj.data.name))
					msg = String.format(context.getString(R.string.prompt_present_meeting),
							jsonObj.data.name);
				ToastUtils.getInstance().showToast(context, msg);
			}
		}
	}

	/**
	 * 请求解散临时会议
	 */
	@Override
	public void dismissBrainStorm(MeetingUserInfo info) {
		BrainStormDetailContract.Model brainStormBiz = getModel();
		if (brainStormBiz == null) return;
		brainStormBiz.requestForDismissMeeting(info,
				new JsonHttpResponseHandler<BrainStormCreateResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            BrainStormCreateResponse jsonObj) {
						BrainStormDetailContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_dismiss_brain_storm_success);
					}

					@Override
					public void onCancel() {
						BrainStormDetailContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
					}

					@Override
					public void onHandleFailure(String errorMsg) {
						BrainStormDetailContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_dismiss_brain_storm_failure);
					}
				});
	}


}
