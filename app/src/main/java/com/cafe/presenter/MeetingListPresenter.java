package com.cafe.presenter;

import android.app.DialogFragment;
import android.content.Context;

import com.aiviews.dialog.AlertDialogInfo;
import com.aiviews.dialog.OnClickDialogBtnListener;
import com.cafe.R;
import com.cafe.common.PreManager;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.ResultCode;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.LogUserResponse;
import com.cafe.data.account.LogoutResponse;
import com.cafe.data.meeting.CancelMeetingResponse;
import com.cafe.data.meeting.DismissMeetingResponse;
import com.cafe.data.meeting.JoinMeetingResponse;
import com.cafe.data.meeting.JoinType;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingListResponse;
import com.cafe.data.meeting.MeetingState;
import com.cafe.data.meeting.MeetingType;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.QueryMeetingResponse;
import com.cafe.data.meeting.QueryMeetingUserResponse;
import com.cafe.data.meeting.QuitMeetingResponse;
import com.cafe.model.meeting.MeetingListBiz;

import org.justin.utils.common.LogUtils;
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
		setModel(initModel());
	}

	@Override
	public MeetingListContract.Model initModel() {
		return new MeetingListBiz(context);
	}

	/**
	 * 获取用户信息
	 */
	@Override
	public void getUserInfo() {
		MeetingListContract.View view = getView();
		if (view == null) return;
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.getUserInfo(new JsonHttpResponseHandler<LogUserResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, LogUserResponse jsonObj) {
				// 处理获取用户信息成功
				handleSuccess(jsonObj);
			}

			@Override
			public void onCancel() {
//				MeetingListContract.View view = getView();
//				if (view == null) return;
//				view.dismissLoadingProgress();
//				ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
			}

			@Override
			public void onHandleFailure(String errorMsg) {
//				MeetingListContract.View view = getView();
//				if (view == null) return;
//				view.dismissLoadingProgress();
//				ToastUtils.getInstance().showToast(context, R.string.prompt_logout_failure);
			}
		});

	}

	// 处理获取用户信息成功
	private void handleSuccess(LogUserResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		if (response.data == null) return;
		view.setUserInfo(response.data);
	}

	/**
	 * 加载会议列表
	 */
	@Override
	public void loadMeetingList() {
//		MeetingListContract.View view = getView();
//		if (view == null) return;
//		view.showLoadingProgress(null);
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.loadMeetingList(new JsonHttpResponseHandler<MeetingListResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers,
			                            final MeetingListResponse jsonObj) {
				handleSuccess(jsonObj);
//				ThreadUtils.runOnUIThread(new Runnable() {
//					@Override
//					public void run() {
//
//					}
//				}, 1000);
			}

			@Override
			public void onCancel() {
//				MeetingListContract.View view = getView();
//				if (view == null) return;
//				view.dismissLoadingProgress();
				//TODO: 没有网络的情况会终止请求,显示点击屏幕重新加载

			}

			@Override
			public void onHandleFailure(String errorMsg) {
//				MeetingListContract.View view = getView();
//				if (view == null) return;
//				view.dismissLoadingProgress();
				// TODO:请求失败, 显示点击屏幕重新加载
			}
		});
	}

	// 处理数据返回成功的情况
	private void handleSuccess(MeetingListResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
//		view.dismissLoadingProgress();
		if (response.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
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
		final MeetingListContract.View view = getView();
		if (view == null) return;
		// 弹出对话框
		AlertDialogInfo info = new AlertDialogInfo();
		info.title = context.getString(R.string.dialog_title_logout);
		info.content = context.getString(R.string.dialog_content_logout);
		view.showAlertDialog(info, new OnClickDialogBtnListener<Void>() {
			@Override
			public void onClickEnsure(DialogFragment df, Void aVoid) {
				df.dismiss();
				doLogout(view);
			}

			@Override
			public void onClickCancel(DialogFragment df) {
				df.dismiss();
			}
		});

	}

	/**
	 * 执行登出任务
	 */
	private void doLogout(MeetingListContract.View view) {
		view.showLoadingProgress(null);
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.logout(new JsonHttpResponseHandler<LogoutResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, LogoutResponse jsonObj) {
				// 处理登出成功
				handleSuccess(jsonObj);
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
	private void handleSuccess(LogoutResponse jsonObj) {
		// 清除token
		PreManager.setToken(context, "");
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
		if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		ToastUtils.getInstance().showToast(context, R.string.prompt_logout_success);
		// 跳转到登录界面
		view.skipToLoginActivity();
	}

	/**
	 * 跳转到搜索界面
	 */
	@Override
	public void search() {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.skipToSearchActivity();
	}

	@Override
	public void scanQRCode() {
		if (getView() != null) {
			getView().skipToScanQRCodeActivity();
		}
	}

	/**
	 * 跳转到创建主题会议界面
	 */
	@Override
	public void createTheme() {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.skipToCreateMeetingActivity();
	}

	/**
	 * 处理创建主题会议返回
	 */
	@Override
	public void handleCreateTheme() {

	}

	/**
	 * 处理扫描二维码返回：加入会议
	 */
	@Override
	public void handleScanQRCode() {

	}

	/**
	 * 提示摇一摇创建头脑风暴
	 */
	@Override
	public void createBrainStorm() {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.promptShakePhone();
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
	public void quitMeeting(final MeetingUserInfo meetingInfo) {
		final MeetingListContract.View view = getView();
		if (view == null) return;
		// 弹出对话框
		AlertDialogInfo info = new AlertDialogInfo();
		info.title = context.getString(R.string.dialog_title_quit);
		info.content = String.format(context.getString(R.string.dialog_content_quit),
				meetingInfo.name);
		view.showAlertDialog(info, new OnClickDialogBtnListener<Void>() {
			@Override
			public void onClickEnsure(DialogFragment df, Void aVoid) {
				df.dismiss();
				doQuit(meetingInfo, view);
			}

			@Override
			public void onClickCancel(DialogFragment df) {
				df.dismiss();
			}
		});
	}

	/**
	 * 执行加入会议任务
	 */
	private void doQuit(final MeetingUserInfo meetingInfo, MeetingListContract.View view) {
		view.showLoadingProgress(null);
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.quitMeeting(meetingInfo, new JsonHttpResponseHandler<QuitMeetingResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, QuitMeetingResponse jsonObj) {
				// 处理退出会议成功
				handleSuccess(meetingInfo, jsonObj);
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
				ToastUtils.getInstance().showToast(context, R.string.prompt_quit_failure);
			}
		});
	}

	/**
	 * 处理解散会议成功
	 */
	private void handleSuccess(MeetingUserInfo info, QuitMeetingResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
		if (response.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		if (response.data != null && response.data.result) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_quit_success);
			view.refreshAfterQuit(info);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_quit_failure);
		}
	}

	/**
	 * 解散会议
	 */
	@Override
	public void dismissMeeting(final MeetingUserInfo meetingInfo) {
		final MeetingListContract.View view = getView();
		if (view == null) return;
		// 弹出对话框
		AlertDialogInfo info = new AlertDialogInfo();
		info.title = context.getString(R.string.dialog_title_dismiss);
		info.content = String.format(context.getString(R.string.dialog_content_dismiss),
				meetingInfo.name);
		view.showAlertDialog(info, new OnClickDialogBtnListener<Void>() {
			@Override
			public void onClickEnsure(DialogFragment df, Void aVoid) {
				df.dismiss();
				doDismiss(meetingInfo, view);
			}

			@Override
			public void onClickCancel(DialogFragment df) {
				df.dismiss();
			}
		});
	}

	/**
	 * 执行加入会议任务
	 */
	private void doDismiss(final MeetingUserInfo meetingInfo, MeetingListContract.View view) {
		view.showLoadingProgress(null);
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.dismissMeeting(meetingInfo, new JsonHttpResponseHandler<DismissMeetingResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, DismissMeetingResponse jsonObj) {
				// 处理加入会议成功
				handleSuccess(meetingInfo, jsonObj);
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
				ToastUtils.getInstance().showToast(context, R.string.prompt_dismiss_failure);
			}
		});
	}

	/**
	 * 处理解散会议成功
	 */
	private void handleSuccess(MeetingUserInfo info, DismissMeetingResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
		if (response.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		if (response.data != null && response.data.result) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_dismiss_success);
			view.refreshAfterDismiss(info);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_dismiss_failure);
		}
	}

	// TODO:通过扫描二维码加入会议
	@Override
	public void joinMeetingByQRCode(String result) {
		final MeetingListContract.View view = getView();
		if (view == null) return;
		view.showLoadingProgress(context.getString(R.string.prompt_query_meeting_detail));
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		int meetingId = -1;
		try {
			meetingId = Integer.parseInt(result);
		} catch (Exception e) {
			LogUtils.e(TAG, "Exception", e);
		}
		meetingListBiz.getMeetingDetail(meetingId, new JsonHttpResponseHandler<QueryMeetingUserResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers,
			                            QueryMeetingUserResponse jsonObj) {
				// TODO:包一个接口来查询一下用户会议详情，返回是否本人创建
				MeetingListContract.View view = getView();
				if (view == null) return;
				view.dismissLoadingProgress();
				if (jsonObj.desc.result_code == ResultCode.LOGIN_FAILURE) {
					// 处理登录失效
					view.skipToLoginActivity();
					return;
				}
				doJoin(jsonObj.data, view);
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
				ToastUtils.getInstance().showToast(context,
						R.string.prompt_query_meeting_detail_failure);
			}
		});
	}

	/**
	 * 加入会议
	 */
	// TODO:为什么加入会议对话框弹出的时候界面按钮会闪动成退出会议
	@Override
	public void joinMeeting(final MeetingUserInfo meetingInfo) {
		final MeetingListContract.View view = getView();
		if (view == null) return;
		// 弹出对话框
		AlertDialogInfo info = new AlertDialogInfo();
		info.title = context.getString(R.string.dialog_title_join);
		info.content = String.format(context.getString(R.string.dialog_content_join),
				meetingInfo.name);
		view.showAlertDialog(info, new OnClickDialogBtnListener<Void>() {
			@Override
			public void onClickEnsure(DialogFragment df, Void aVoid) {
				df.dismiss();
				doJoin(meetingInfo, view);
			}

			@Override
			public void onClickCancel(DialogFragment df) {
				df.dismiss();
			}
		});

	}

	/**
	 * 执行加入会议任务
	 */
	private void doJoin(final MeetingUserInfo meetingInfo, MeetingListContract.View view) {
		view.showLoadingProgress(context.getString(R.string.prompt_joining));
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.joinMeeting(meetingInfo, new JsonHttpResponseHandler<JoinMeetingResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers,
			                            JoinMeetingResponse jsonObj) {
				// 处理加入会议成功
				handleSuccess(meetingInfo, jsonObj);
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
				ToastUtils.getInstance().showToast(context, R.string.prompt_join_failure);
			}
		});
	}

	/**
	 * 处理加入会议成功
	 */
	private void handleSuccess(MeetingUserInfo info, JoinMeetingResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
		if (response.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		if (response.data != null && response.data.result) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_join_success);
			view.refreshAfterJoin(info);

		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_join_failure);
		}
		// TODO:跳转到会议详情界面
//		view.skipToLoginActivity();
	}

	/**
	 * 取消会议
	 */
	@Override
	public void cancelMeeting(final MeetingUserInfo meetingInfo) {
		final MeetingListContract.View view = getView();
		if (view == null) return;
		// 弹出对话框
		AlertDialogInfo info = new AlertDialogInfo();
		info.title = context.getString(R.string.dialog_title_cancel);
		info.content = String.format(context.getString(R.string.dialog_content_cancel),
				meetingInfo.name);
		view.showAlertDialog(info, new OnClickDialogBtnListener<Void>() {
			@Override
			public void onClickEnsure(DialogFragment df, Void aVoid) {
				df.dismiss();
				doCancel(meetingInfo, view);
			}

			@Override
			public void onClickCancel(DialogFragment df) {
				df.dismiss();
			}
		});
	}

	/**
	 * 展示会议过程详情
	 */
	@Override
	public void showMeetingProcedure(MeetingUserInfo meetingInfo) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		if (meetingInfo.state != MeetingState.PROGRESS) {
			// TODO: 提示只有正在进行的会议才能展示详情？
			return;
		}
		if (meetingInfo.type == MeetingType.THEME.getId())
			view.skipToThemeDetailActivity(meetingInfo);
		else if (meetingInfo.type == MeetingType.BRAIN_STORM.getId())
			view.skipToBrainStormActivity(meetingInfo);
	}

	/**
	 * 执行取消预约会议任务
	 */
	private void doCancel(final MeetingUserInfo meetingInfo, MeetingListContract.View view) {
		view.showLoadingProgress(null);
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.cancelMeeting(meetingInfo, new JsonHttpResponseHandler<CancelMeetingResponse>() {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers, CancelMeetingResponse jsonObj) {
				// 处理取消预约会议成功
				handleSuccess(meetingInfo, jsonObj);
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
				ToastUtils.getInstance().showToast(context, R.string.prompt_cancel_failure);
			}
		});
	}

	/**
	 * 处理加入会议成功
	 */
	private void handleSuccess(MeetingUserInfo info, CancelMeetingResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
		if (response.desc.result_code == ResultCode.LOGIN_FAILURE) {
			// 处理登录失效
			view.skipToLoginActivity();
			return;
		}
		if (response.data != null && response.data.result) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_cancel_success);
			view.refreshAfterCancel(info);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_cancel_failure);
		}
	}


	//********************************下面是测试


	// 测试会议列表
	@Override
	public void loadMeetingListTest() {
		getView().showLoadingProgress(null);
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
