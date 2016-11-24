package com.cafe.presenter;

import android.content.Context;

import com.cafe.R;
import com.cafe.common.PreManager;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.LogoutResponse;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingListResponse;
import com.cafe.data.meeting.MeetingState;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.model.meeting.MeetingListBiz;

import org.justin.utils.common.ToastUtils;
import org.justin.utils.thread.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 会议列表界面Presenter
 * Created by Justin Z on 2016/11/8.
 * 502953057@qq.com
 */

public class MeetingListPresenter extends MVPPresenter<MeetingListContract.View,
		MeetingListContract.Model> implements MeetingListContract.Presenter {

	private final static String TAG = MeetingListPresenter.class.getSimpleName();

	private Context context;

	public MeetingListPresenter(Context context) {
		this.context = context;
		setModel();
	}

	@Override
	public MeetingListContract.Model initModel() {
		return new MeetingListBiz(context);
	}


	/**
	 * 加载会议列表
	 */
	@Override
	public void loadMeetingList() {
		getView().showLoadingProgress();
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.loadMeetingList(new JsonHttpResponseHandler<MeetingListResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers,
			                            final MeetingListResponse jsonObj) {
				ThreadUtils.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						handleSuccess(jsonObj);
					}
				}, 1000);
			}

			@Override
			public void onCancel() {
				MeetingListContract.View view = getView();
				if (view == null) return;
				view.dismissLoadingProgress();
				//TODO: 没有网络的情况会终止请求,显示点击屏幕重新加载

			}

			@Override
			public void onHandleFailure(String errorMsg) {
				MeetingListContract.View view = getView();
				if (view == null) return;
				view.dismissLoadingProgress();
				// TODO:请求失败, 显示点击屏幕重新加载
			}
		});
	}

	// 处理数据返回成功的情况
	private void handleSuccess(MeetingListResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
		if (response.data.meetingInfos == null || response.data.meetingInfos.size() == 0) {
			// TODO:会议列表数据为空，显示提示
			return;
		}
		// 界面加载列表
		view.loadMeetingList(response.data.meetingInfos);
	}

	/**
	 * 登出系统
	 */
	@Override
	public void logout() {
		getView().showLoadingProgress();
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.logout(new JsonHttpResponseHandler<LogoutResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, LogoutResponse jsonObj) {
				// 处理登出成功
				handleSuccess();

			}
			@Override
			public void onCancel() {
				MeetingListContract.View view = getView();
				if (view == null) return;
				view.dismissLoadingProgress();
				ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
				MeetingListContract.View view = getView();
				if (view == null) return;
				view.dismissLoadingProgress();
				ToastUtils.getInstance().showToast(context, R.string.prompt_logout_failure);
			}
		});

	}

	/**
	 * 处理登出成功
	 */
	private void handleSuccess(){
		// 清除token
		PreManager.setToken(context, "");
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
		ToastUtils.getInstance().showToast(context, R.string.prompt_logout_success);
		// 跳转到登录界面
		view.skipToLoginActivity();
	}

	@Override
	public void search() {
		ToastUtils.getInstance().showToast(context, "搜索");
	}

	@Override
	public void scanQRCode() {
		ToastUtils.getInstance().showToast(context, "扫描二维码");
	}

	@Override
	public void createTheme() {

	}

	@Override
	public void createBrainStorm() {

	}

	/**
	 * 显示会议二维码
	 */
	@Override
	public void showQRCode(MeetingUserInfo info) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.showQRcodeDialog(info);
	}

	/**
	 * 退出会议
	 */
	@Override
	public void quitMeeting(MeetingUserInfo meetingInfo) {

	}

	/**
	 * 解散会议
	 */
	@Override
	public void dismissMeeting(MeetingUserInfo meetingInfo) {

	}

	/**
	 * 加入会议
	 */
	@Override
	public void joinMeeting(MeetingUserInfo meetingInfo) {

	}

	/**
	 * 取消会议
	 */
	@Override
	public void cancelMeeting(MeetingUserInfo meetingInfo) {

	}

	// 测试会议列表
	@Override
	public void loadMeetingListTest() {
		getView().showLoadingProgress();
		ThreadUtils.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				MeetingListContract.View view = getView();
				if (view == null) return;
				view.dismissLoadingProgress();
				view.loadMeetingList(getMeetingInfoList());
			}
		}, 2000);
	}

	// TODO:构造会议列表测试数据
	private List<MeetingUserInfo> getMeetingInfoList() {
		List<MeetingUserInfo> meetingInfos = new ArrayList<>();
		MeetingUserInfo info;
		// 正在进行的
		info = new MeetingUserInfo();
		info.state = MeetingState.PROGRESS;
		info.name = "市场部会议1";
		info.startTime = "2016-11-10 08:10:00";
		info.participatedFlag = false;
		info.createdFlag = true;
		info.meetingRoomName = "市场部会议室1";
		meetingInfos.add(info);
		info = new MeetingUserInfo();
		info.state = MeetingState.PROGRESS;
		info.name = "市场部会议2";
		info.startTime = "2016-11-10 09:10:00";
		info.participatedFlag = true;
		info.createdFlag = false;
		info.meetingRoomName = "市场部会议室2";
		meetingInfos.add(info);
		// 预约的
		info = new MeetingUserInfo();
		info.state = MeetingState.APPOINTMENT;
		info.name = "技术部会议1";
		info.startTime = "2016-11-10 10:10:00";
		info.participatedFlag = true;
		info.createdFlag = false;
		info.meetingRoomName = "技术部会议室1";
		meetingInfos.add(info);
		info = new MeetingUserInfo();
		info.state = MeetingState.APPOINTMENT;
		info.name = "技术部会议2";
		info.startTime = "2016-11-10 11:11:00";
		info.participatedFlag = true;
		info.createdFlag = false;
		info.meetingRoomName = "技术部会议室2";
		meetingInfos.add(info);
		info = new MeetingUserInfo();
		info.state = MeetingState.APPOINTMENT;
		info.name = "技术部会议3";
		info.startTime = "2016-11-10 12:11:00";
		info.participatedFlag = true;
		info.createdFlag = false;
		info.meetingRoomName = "技术部会议室3";
		meetingInfos.add(info);
		// 历史的
		info = new MeetingUserInfo();
		info.state = MeetingState.HISTORY;
		info.name = "人事部会议1";
		info.startTime = "2016-11-09 12:11:00";
		info.participatedFlag = true;
		info.createdFlag = false;
		meetingInfos.add(info);
		info = new MeetingUserInfo();
		info.state = MeetingState.HISTORY;
		info.name = "人事部会议2";
		info.startTime = "2016-11-09 10:11:00";
		info.participatedFlag = false;
		info.createdFlag = true;
		meetingInfos.add(info);
		info = new MeetingUserInfo();
		info.state = MeetingState.HISTORY;
		info.name = "人事部会议3";
		info.startTime = "2016-11-09 09:11:00";
		info.participatedFlag = true;
		info.createdFlag = true;
		meetingInfos.add(info);
		info = new MeetingUserInfo();
		info.state = MeetingState.HISTORY;
		info.name = "人事部会议4";
		info.startTime = "2016-11-09 08:11:00";
		info.participatedFlag = true;
		info.createdFlag = true;
		meetingInfos.add(info);
		return meetingInfos;
	}


}
