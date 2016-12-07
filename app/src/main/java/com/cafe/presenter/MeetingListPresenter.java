package com.cafe.presenter;

import android.app.DialogFragment;
import android.content.Context;

import com.aiviews.dialog.AlertDialogInfo;
import com.aiviews.dialog.OnClickDialogBtnListener;
import com.cafe.R;
import com.cafe.common.PreManager;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.LogUserResponse;
import com.cafe.data.account.LogoutResponse;
import com.cafe.data.meeting.BrainStormDismissRequest;
import com.cafe.data.meeting.BrainStormDismissResponse;
import com.cafe.data.meeting.CancelMeetingResponse;
import com.cafe.data.meeting.DismissMeetingResponse;
import com.cafe.data.meeting.JoinMeetingResponse;
import com.cafe.data.meeting.MeetingListResponse;
import com.cafe.data.meeting.MeetingState;
import com.cafe.data.meeting.MeetingType;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.QueryMeetingUserResponse;
import com.cafe.data.meeting.QuitMeetingResponse;
import com.cafe.data.meeting.BrainStormCreateResponse;
import com.cafe.model.meeting.MeetingListBiz;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.TimeUtils;
import org.justin.utils.common.ToastUtils;

import java.util.Date;

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
		view.showLoadingProgress(null);
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.getUserInfo(new JsonHttpResponseHandler<LogUserResponse>(context) {
			@Override
			public void onHandleSuccess(int statusCode, Header[] headers,
			                            LogUserResponse jsonObj) {
				// 处理获取用户信息成功
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
				ToastUtils.getInstance().showToast(context, R.string.prompt_get_user_info_failure);
			}
		});

	}

	// 处理获取用户信息成功
	private void handleSuccess(LogUserResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		view.dismissLoadingProgress();
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
		meetingListBiz.loadMeetingList(new JsonHttpResponseHandler<MeetingListResponse>(context) {
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

			}

			@Override
			public void onHandleFailure(String errorMsg) {
//				MeetingListContract.View view = getView();
//				if (view == null) return;
//				view.dismissLoadingProgress();
			}
		});
	}

	// 处理数据返回成功的情况
	private void handleSuccess(MeetingListResponse response) {
		MeetingListContract.View view = getView();
		if (view == null) return;
//		view.dismissLoadingProgress();
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
		meetingListBiz.logout(new JsonHttpResponseHandler<LogoutResponse>(context) {
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
	 * 摇一摇手机处理创建或解散临时会议
	 */
	@Override
	public void shakePhoneForBrainStorm() {
		MeetingListContract.View view = getView();
		if (view == null) return;
		// TODO:停止监听摇一摇,避免多次摇晃
		if (view.isStartShake()) view.stopShake();
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.getIsAtSomeMeeting(
				new JsonHttpResponseHandler<QueryMeetingUserResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            QueryMeetingUserResponse jsonObj) {
						handleSuccess(jsonObj);
					}

					@Override
					public void onCancel() {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
					}

					@Override
					public void onHandleFailure(String errorMsg) {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_present_meeting_failure);
					}
				});

	}

	/**
	 * 处理查询用户在会状态成功
	 */
	private void handleSuccess(QueryMeetingUserResponse jsonObj) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		// 用户不在会才能创建临时会议
		if (jsonObj.data.id == -1) {
			LogUtils.i(TAG, "创建头脑风暴-->");
			createBrainStorm();
		}
		// 用户已经在会
		else {
			// 如果用户已经在头脑风暴会议中则退出会议
			if (jsonObj.data.type == MeetingType.BRAIN_STORM.getId()) {
				LogUtils.i(TAG, "头脑风暴解散-->");
				MeetingUserInfo info = new MeetingUserInfo();
				info.id = jsonObj.data.id;
				dismissBrainStorm(info);
			}
			// 提示用户已经在主题会议中
			else if (jsonObj.data.type == MeetingType.THEME.getId()) {
				LogUtils.i(TAG, "头脑风暴操作提示已经在主题会议中-->");
				if (!view.isStartShake()) view.startShake();
				String msg = String.format(context.getString(R.string.prompt_present_meeting),
						jsonObj.data.name);
				ToastUtils.getInstance().showToast(context, msg);
			}
		}

	}

	/**
	 * 请求创建头脑风暴
	 */
	@Override
	public void createBrainStorm() {
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.requestCreateBrainStorm(
				new JsonHttpResponseHandler<BrainStormCreateResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            BrainStormCreateResponse jsonObj) {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_create_brain_storm_success);
					}

					@Override
					public void onCancel() {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
					}

					@Override
					public void onHandleFailure(String errorMsg) {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_create_brain_storm_failure);
					}
				});

	}

	/**
	 * 请求解散临时会议
	 */
	@Override
	public void dismissBrainStorm(MeetingUserInfo info) {
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.requestDismissBrainStorm(info,
				new JsonHttpResponseHandler<BrainStormDismissResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            BrainStormDismissResponse jsonObj) {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_dismiss_brain_storm_success);
					}

					@Override
					public void onCancel() {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
					}

					@Override
					public void onHandleFailure(String errorMsg) {
						MeetingListContract.View view = getView();
						if (view == null) return;
						if (!view.isStartShake()) view.startShake();
						ToastUtils.getInstance().showToast(context,
								R.string.prompt_dismiss_brain_storm_failure);
					}
				});
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
		// TODO:这里需要对会议类型做判断，临时会议和主题会议的退出接口不同
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
		meetingListBiz.quitMeeting(meetingInfo,
				new JsonHttpResponseHandler<QuitMeetingResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            QuitMeetingResponse jsonObj) {
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
		meetingListBiz.dismissMeeting(meetingInfo,
				new JsonHttpResponseHandler<DismissMeetingResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            DismissMeetingResponse jsonObj) {
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
		if (response.data != null && response.data.result) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_dismiss_success);
			view.refreshAfterDismiss(info);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_dismiss_failure);
		}
	}

	/**
	 * 通过扫描二维码加入会议
	 */
	@Override
	public void joinMeetingByQRCode(String result) {
//		final MeetingListContract.View view = getView();
//		if (view == null) return;
//		view.showLoadingProgress(context.getString(R.string.prompt_query_meeting_detail));
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		int meetingId = -1;
		try {
			meetingId = Integer.parseInt(result);
		} catch (Exception e) {
			LogUtils.e(TAG, "Exception", e);
		}
		final int id = meetingId;
		meetingListBiz.getMeetingDetail(id,
				new JsonHttpResponseHandler<QueryMeetingUserResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            QueryMeetingUserResponse jsonObj) {
						jsonObj.data.id = id;
						doJoin(jsonObj.data);
					}

					@Override
					public void onCancel() {
//				MeetingListContract.View view = getView();
//				if (view == null) return;
//				view.dismissLoadingProgress();
						ToastUtils.getInstance().showToast(context, R.string.prompt_no_network);
					}

					@Override
					public void onHandleFailure(String errorMsg) {
//				MeetingListContract.View view = getView();
//				if (view == null) return;
//				view.dismissLoadingProgress();
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
				doJoin(meetingInfo);
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
	private void doJoin(final MeetingUserInfo meetingInfo) {
		final MeetingListContract.View view = getView();
		if (view == null) return;
		view.showLoadingProgress(context.getString(R.string.prompt_joining));
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.joinMeeting(meetingInfo,
				new JsonHttpResponseHandler<JoinMeetingResponse>(context) {
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
		if (response.data != null && response.data.result) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_join_success);
			view.refreshAfterJoin(info);

		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_join_failure);
		}
		// 是否需要跳转到会议详情界面
//		showMeetingProcedure(info);
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
	 * 跳转到会议详情界面
	 */
	@Override
	public void showMeetingProcedure(MeetingUserInfo meetingInfo) {
		MeetingListContract.View view = getView();
		if (view == null) return;
		if (meetingInfo.state != MeetingState.PROGRESS) {
			return;
		}
		long startTime = TimeUtils.dateToTimeStamp(meetingInfo.startTime,
				TimeUtils.Template.YMDHMS) / 1000;
		long currentTime = new Date().getTime() / 1000;
		LogUtils.i(TAG, "会议开始时间-->" + startTime);
		LogUtils.i(TAG, "当前时间-->" + currentTime);
		if (currentTime < startTime) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_meeting_not_start);
			return;
		}
		// 没有加入的会议是不能查看详情的
		if (meetingInfo.participatedFlag) {
			if (meetingInfo.type == MeetingType.THEME.getId())
				// 跳转到主题会议详情界面
				view.skipToThemeDetailActivity(meetingInfo);
			else if (meetingInfo.type == MeetingType.BRAIN_STORM.getId())
				// 跳转到临时会议详情界面
				view.skipToBrainStormActivity(meetingInfo);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_not_participated);
		}
	}

	/**
	 * 执行取消预约会议任务
	 */
	private void doCancel(final MeetingUserInfo meetingInfo, MeetingListContract.View view) {
		view.showLoadingProgress(null);
		MeetingListContract.Model meetingListBiz = getModel();
		if (meetingListBiz == null) return;
		meetingListBiz.cancelMeeting(meetingInfo,
				new JsonHttpResponseHandler<CancelMeetingResponse>(context) {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            CancelMeetingResponse jsonObj) {
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
		if (response.data != null && response.data.result) {
			ToastUtils.getInstance().showToast(context, R.string.prompt_cancel_success);
			view.refreshAfterCancel(info);
		} else {
			ToastUtils.getInstance().showToast(context, R.string.prompt_cancel_failure);
		}
	}


}
