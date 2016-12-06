package com.cafe.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.aiviews.anim.AnimationImageLoadingListener;
import com.aiviews.imageview.RoundImageView;
import com.aiviews.toolbar.ToolbarActivity;
import com.cafe.R;
import com.cafe.adapter.MeetingListRvAdapter;
import com.cafe.base.ActivityManager;
import com.cafe.common.ImageLoaderManager;
import com.cafe.common.IntentExtra;
import com.cafe.common.ShakePhoneUtils;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.UserInfo;
import com.cafe.data.meeting.MeetingState;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.fragment.QRCodeDialog;
import com.cafe.presenter.MeetingListPresenter;
import com.cafe.service.CheckService;
import com.cafe.service.FunfManagerService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ResourcesUtils;
import org.justin.utils.common.ToastUtils;
import org.justin.utils.system.AppUtils;
import org.justin.utils.system.DisplayUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 会议列表界面
 * Created by Justin Z on 2016/11/7.
 * 502953057@qq.com
 */

public class MeetingListActivity extends MVPActivity<MeetingListContract.View,
		MeetingListPresenter> implements MeetingListContract.View, View.OnClickListener, EasyPermissions.PermissionCallbacks {

	private final static String TAG = MeetingListActivity.class.getSimpleName();
	private static final int PERMISSION_CAMERA_REQUEST_CODE = 999;
	private static final int REQUEST_QR_CODE = 111;
	private static final int REQUEST_CREATE_MEETING_CODE = 888;

	private final static int MSG_GET_MEETING_LIST = 0x0020;

	private final static long GET_MEETING_LIST_PERIOD = 1000 * 5;

	/**
	 * 显示会议二维码对话框TAG
	 */
	private final static String TAG_DIALOG_QRCODE = "tag_dialog_qrcode";

	private CoordinatorLayout meetingListCl;
	/**
	 * 用户头像
	 */
	private RoundImageView userPortraitIv;
	/**
	 * 用户名字
	 */
	private TextView userNameTv;
	/**
	 * 用户工号
	 */
	private TextView workNumberTv;
	/**
	 * 会议列表RecyclerView
	 */
	private RecyclerView meetingListRv;
	private MeetingListRvAdapter meetingListRvAdapter;
	private LinearLayoutManager layoutManager;

	// TODO:定时任务，轮询会议列表
	private Timer timer;
	private TimerTask timerTask;

	/**
	 * 检查FUNF的service
	 */
	private Intent checkIntent;

	private long firstTime = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_list);
		checkIntent = new Intent(this, CheckService.class);
		setToolbar();
		initViews();
		setMeetingListRv();
		// 获取用户信息
		getPresenter().getUserInfo();
		// 轮询会议列表
		getMeetingList();
		// 开始手机摇一摇检测
		startShakePhone();
		checkCameraPermission();
		// 启动检查FUNF的服务
		startService(checkIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 停止手机摇一摇检测
		stopShakePhone();
		stopService(checkIntent);
		if (timerTask != null)
			timerTask.cancel();
		if (timer != null)
			timer.cancel();

	}

	private void startShakePhone() {
		ShakePhoneUtils.getInstance().startShake(this, new ShakePhoneUtils.OnShakeListener() {
			@Override
			public void onShake() {
				LogUtils.i(TAG, "-->手机摇一摇");
			}
		});
	}

	private void stopShakePhone() {
		ShakePhoneUtils.getInstance().stopShake();
	}

	/**
	 * 轮询会议列表
	 */
	private void getMeetingList() {
		final RefreshHandler handler = new RefreshHandler(this);
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				handler.obtainMessage(MSG_GET_MEETING_LIST).sendToTarget();
			}
		};
		timer.schedule(timerTask, 0, GET_MEETING_LIST_PERIOD);

	}

	/**
	 * Handler处理消息
	 */
	private static class RefreshHandler extends Handler {

		private WeakReference<MeetingListActivity> reference;

		RefreshHandler(MeetingListActivity activity) {
			reference = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MeetingListActivity activity = reference.get();
			if (activity == null) return;
			switch (msg.what) {
				case MSG_GET_MEETING_LIST:
					activity.getPresenter().loadMeetingList();
					break;
			}

		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.theme_meeting_fab:
				// 创建主题会议
				getPresenter().createTheme();
				break;
			case R.id.brain_storm_fab:
				// 提示摇一摇手机创建头脑风暴
				getPresenter().createBrainStorm();
				break;
		}

	}

	/**
	 * 处理其他界面的返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
					PackageManager.PERMISSION_GRANTED) {
				//	getPresenter().loadCamera();
			}
		} else if (requestCode == REQUEST_QR_CODE) {

			if (null != data) {
				Bundle bundle = data.getExtras();
				if (bundle == null) {
					return;
				}
				if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
					String result = bundle.getString(CodeUtils.RESULT_STRING);
					LogUtils.i(TAG, "扫描二维码成功-->" + result);
					getPresenter().joinMeetingByQRCode(result);
					//// TODO: 2016/11/30 scan QR success

				} else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
					ToastUtils.getInstance().showToast(this, R.string.prompt_scan_qrcode_failure);
					//// TODO: 2016/11/30 SCAN QR fail
				}
			}
		} else if (requestCode == REQUEST_CREATE_MEETING_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				//// TODO: 2016/11/30 create meeting success
				LogUtils.i(TAG, "创建会议成功,重新加载列表-->");
				getPresenter().loadMeetingList();
			}
		}


	}

	@Override
	public MeetingListPresenter initPresenter() {
		return new MeetingListPresenter(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		// Forward results to EasyPermissions
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@AfterPermissionGranted(PERMISSION_CAMERA_REQUEST_CODE)
	private void methodRequiresTwoPermission() {
		String[] perms = {Manifest.permission.CAMERA};
		if (EasyPermissions.hasPermissions(this, perms)) {

		}
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {

		// (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
		// This will display a dialog directing them to enable the permission in app settings.
		if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
			new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
					.setTitle(getString(R.string.title_settings_dialog))
					.setPositiveButton(getString(R.string.ensure))
					.setNegativeButton(getString(R.string.cancel), null)
					.setRequestCode(PERMISSION_CAMERA_REQUEST_CODE)
					.build()
					.show();
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {

	}

	private void checkCameraPermission() {
		// 没有授权的情况下询问用户是否授权
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
				PackageManager.PERMISSION_GRANTED) {
			String[] perms = {Manifest.permission.CAMERA};
			EasyPermissions.requestPermissions(this, getString(R.string.camera_rationale),
					PERMISSION_CAMERA_REQUEST_CODE, perms);
		}
	}

	/**
	 * 初始化界面元素
	 */
	private void initViews() {
		meetingListCl = (CoordinatorLayout) findViewById(R.id.meeting_list_cl);
		View headerView = findViewById(R.id.meeting_list_header);
		AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) headerView.getLayoutParams();
		lp.height = DisplayUtils.getScreenHeight(this) / 4;
		lp.gravity = Gravity.TOP;
		headerView.setLayoutParams(lp);
		userPortraitIv = (RoundImageView) findViewById(R.id.user_portrait_iv);
		userNameTv = (TextView) findViewById(R.id.user_name_tv);
		workNumberTv = (TextView) findViewById(R.id.work_number_tv);
		FloatingActionButton createThemeMeetingFab = (FloatingActionButton) findViewById(R.id.theme_meeting_fab);
		FloatingActionButton createBrainStormFab = (FloatingActionButton) findViewById(R.id.brain_storm_fab);
		createThemeMeetingFab.setOnClickListener(this);
		createBrainStormFab.setOnClickListener(this);
		meetingListRv = (RecyclerView) findViewById(R.id.meeting_list_rv);
	}

	/**
	 * 初始化会议列表
	 */
	private void setMeetingListRv() {
		meetingListRvAdapter = new MeetingListRvAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		meetingListRv.setLayoutManager(layoutManager);
		// 加入分组头部
		final StickyRecyclerHeadersDecoration headersDecor = new
				StickyRecyclerHeadersDecoration(meetingListRvAdapter);
		meetingListRv.addItemDecoration(headersDecor);
		meetingListRv.setAdapter(meetingListRvAdapter);
		// 加入item动画效果
		SlideInDownAnimator animator = new SlideInDownAnimator();
		animator.setAddDuration(500);

		animator.setInterpolator(new BounceInterpolator());
		meetingListRv.setItemAnimator(animator);
		// 点击显示详情
		meetingListRvAdapter.setOnClickItemListener(new MeetingListRvAdapter.OnClickItemListener() {

			@Override
			public void onClick(MeetingUserInfo info) {
				getPresenter().showMeetingProcedure(info);
			}
		});
		// 点击显示二维码
		meetingListRvAdapter.setOnClickQRCodeListener(new MeetingListRvAdapter.OnClickItemListener() {
			@Override
			public void onClick(MeetingUserInfo info) {
				getPresenter().showQRCode(info);
			}
		});
		// 加入会议
		meetingListRvAdapter.setOnClickAddListener(new MeetingListRvAdapter.OnClickItemListener() {
			@Override
			public void onClick(MeetingUserInfo info) {
				LogUtils.i(TAG, "--点击加入会议--");
				getPresenter().joinMeeting(info);
			}
		});
		// 退出会议
		meetingListRvAdapter.setOnClickQuitListener(new MeetingListRvAdapter.OnClickItemListener() {
			@Override
			public void onClick(MeetingUserInfo info) {
				LogUtils.i(TAG, "--点击退出会议--");
				getPresenter().quitMeeting(info);
			}
		});
		// 解散会议
		meetingListRvAdapter.setOnClickDismissListener(new MeetingListRvAdapter.OnClickItemListener() {
			@Override
			public void onClick(MeetingUserInfo info) {
				getPresenter().dismissMeeting(info);
			}
		});
		// 取消会议
		meetingListRvAdapter.setOnClickCancelListener(new MeetingListRvAdapter.OnClickItemListener() {
			@Override
			public void onClick(MeetingUserInfo info) {
				getPresenter().cancelMeeting(info);
			}
		});

	}

	/**
	 * 设置工具栏
	 */
	private void setToolbar() {
		supportToolbar(true);
		setToolbarLeft(R.mipmap.logout, new ToolbarActivity.OnLeftClickListener() {
			@Override
			public void onClick() {
				getPresenter().logout();
			}
		});
		setToolbarRight1(R.mipmap.search, new ToolbarActivity.OnRightClickListener() {
			@Override
			public void onClick() {
				getPresenter().search();
			}
		});
		setToolbarRight2(R.mipmap.scan, new ToolbarActivity.OnRightClickListener() {
			@Override
			public void onClick() {
				getPresenter().scanQRCode();
			}
		});
	}

	/**
	 * 加入成功之后刷新
	 */
	@Override
	public void refreshAfterJoin(MeetingUserInfo info) {
		int position = meetingListRvAdapter.getIndex(info.id);
		// 先移除
		if (position >= 0)
			meetingListRvAdapter.remove(position);
		info.participatedFlag = true;
		int addPosition = position <= 0 ? 0 : position - 1;
		meetingListRvAdapter.add(addPosition, info);
		layoutManager.scrollToPosition(0);
	}

	/**
	 * 退出成功之后刷新
	 */
	@Override
	public void refreshAfterQuit(MeetingUserInfo info) {
		int position = meetingListRvAdapter.getIndex(info.id);
		// 先移除
		if (position >= 0)
			meetingListRvAdapter.remove(position);
		info.participatedFlag = false;
		int addPosition = position <= 0 ? 0 : position - 1;
		meetingListRvAdapter.add(addPosition, info);
		layoutManager.scrollToPosition(0);
	}

	/**
	 * 取消成功之后刷新
	 */
	@Override
	public void refreshAfterCancel(MeetingUserInfo info) {
		// 取消的会议不在列表中展示
		meetingListRvAdapter.remove(meetingListRvAdapter.getIndex(info.id));
	}

	/**
	 * 解散成功之后刷新
	 */
	@Override
	public void refreshAfterDismiss(MeetingUserInfo info) {

		// 解散成功之后的会议直接加入历史会议列表,并从当前的位置移除
		int position = meetingListRvAdapter.getIndex(info.id);
		LogUtils.i(TAG, "当前解散的记录的位置-->" + position);
		// 先移除
		meetingListRvAdapter.remove(position);
		// 找到历史记录的第一条的位置
		int historyOneIndex = meetingListRvAdapter.getHistoryOne();
		LogUtils.i(TAG, "历史记录的第一条位置-->" + historyOneIndex);
		info.participatedFlag = false;
		info.state = MeetingState.HISTORY;
		// 原本列表中没有历史记录就加入列表末尾
		if (historyOneIndex == -1) {
			int size = meetingListRvAdapter.getItemCount();
			meetingListRvAdapter.add(size == 0 ? 0 : size - 1, info);
		} else {
			meetingListRvAdapter.add(historyOneIndex, info);
		}

	}

	/**
	 * 加载会议列表
	 */
	@Override
	public void loadMeetingList(List<MeetingUserInfo> meetingInfos) {
//		meetingListRvAdapter.clear();
//		if (meetingInfos == null || meetingInfos.size() == 0) {
//			LogUtils.i(TAG, "-->没有会议列表数据");
//			return;
//		}
//		int size = meetingInfos.size();
//		for (int i = 0; i < size; i++) {
//			LogUtils.i(TAG, "加入数据-->" + i);
//			meetingListRvAdapter.add(i, meetingInfos.get(i));
//		}

		if (meetingInfos == null || meetingInfos.size() == 0) {
			LogUtils.i(TAG, "-->没有返回会议列表数据");
			return;
		}
		// 获取列表原来的长度
		int itemSize = meetingListRvAdapter.getItemCount();
		// 如果本来没有数据，则依次加入数据
		if (itemSize == 0) {
			int size = meetingInfos.size();
			for (int i = 0; i < size; i++) {
				LogUtils.i(TAG, "加入数据-->" + i);
				meetingListRvAdapter.add(i, meetingInfos.get(i));
				layoutManager.scrollToPosition(0);
			}
		}
		// 如果本来有数据
		else {
			int size = meetingInfos.size();
			for (int i = 0; i < size; i++) {
				MeetingUserInfo info = meetingInfos.get(i);
				int index = meetingListRvAdapter.getIndex(info.id);
				// 如果记录在原来的列表中不存在，则一定是新记录
				if (index == -1) {
					// 正在进行
					if (info.state == MeetingState.PROGRESS) {
						meetingListRvAdapter.add(0, info);
					}
					// 预约的
					else if (info.state == MeetingState.APPOINTMENT) {
						int progressOne = meetingListRvAdapter.getProgressOne();
						int appointOne = meetingListRvAdapter.getAppointOne();
						int historyOne = meetingListRvAdapter.getHistoryOne();
						if (appointOne != -1) {
							meetingListRvAdapter.add(appointOne, info);
						} else {
							if (progressOne == -1 && historyOne != -1) {
								meetingListRvAdapter.add(0, info);
							} else if (progressOne != -1 && historyOne == -1) {
								meetingListRvAdapter.add(meetingListRvAdapter.getItemCount() - 1,
										info);
							}
						}

					}
					// 历史的
					else if (info.state == MeetingState.HISTORY) {
						int historyOne = meetingListRvAdapter.getHistoryOne();
						if (historyOne != -1) {
							meetingListRvAdapter.add(historyOne, info);
						} else {
							meetingListRvAdapter.add(meetingListRvAdapter.getItemCount() - 1,
									info);
						}
					}
				}
				// 如果已经在列表中存在，则需要更新他
				else {
					// 找到列表中id相同的对象
					MeetingUserInfo originInfo = meetingListRvAdapter.getItem(index);
					// 状态不变
					if (info.state == originInfo.state) {
						// 加入会议
						if (info.participatedFlag) {
							if (!originInfo.participatedFlag) {
								meetingListRvAdapter.remove(index);
								info.participatedFlag = true;
								int addPosition = index == 0 ? 0 : index - 1;
								meetingListRvAdapter.add(addPosition, info);
							}
						}
						// 退出会议
						if (!info.participatedFlag) {
							if (originInfo.participatedFlag) {
								meetingListRvAdapter.remove(index);
								info.participatedFlag = false;
								int addPosition = index == 0 ? 0 : index - 1;
								meetingListRvAdapter.add(addPosition, info);
							}
						}
					}
					// 状态变更
					else {
						// 从预约变更到正在进行--会议自动开始
						if (info.state == MeetingState.PROGRESS) {
							if (originInfo.state == MeetingState.APPOINTMENT) {
								meetingListRvAdapter.remove(index);
								meetingListRvAdapter.add(0, info);
							}
						}
						// 从正在进行变更到历史--解散
						else if (info.state == MeetingState.HISTORY) {
							if (originInfo.state == MeetingState.PROGRESS) {
								meetingListRvAdapter.remove(index);
								int historyOne = meetingListRvAdapter.getHistoryOne();
								// 原本列表中没有历史记录就加入列表末尾
								if (historyOne == -1) {
									int itemCount = meetingListRvAdapter.getItemCount();
									meetingListRvAdapter.add(itemCount == 0 ? 0 : itemCount - 1, info);
								} else {
									meetingListRvAdapter.add(historyOne, info);
								}
							}
						}


					}


				}


			}
			layoutManager.scrollToPosition(0);
		}


	}

	/**
	 * 设置用户信息显示
	 */
	@Override
	public void setUserInfo(UserInfo userInfo) {
		userPortraitIv.setImageDrawable(ResourcesUtils.getDrawable(R.mipmap.user1));
		//加载用户头像
		ImageLoader.getInstance().displayImage(userInfo.userPortrait, userPortraitIv,
				ImageLoaderManager.getImageOptions(this), new AnimationImageLoadingListener());
		userNameTv.setText(userInfo.userName);
		workNumberTv.setText(userInfo.workNumber);
	}

	/**
	 * 提示创建头脑风暴会议
	 */
	@Override
	public void promptShakePhone() {
		Snackbar.make(meetingListCl, getString(R.string.prompt_shake_phone), Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();
	}

	/**
	 * 显示二维码对话框
	 */
	@Override
	public void showQRcodeDialog(MeetingUserInfo meetingInfo) {
		QRCodeDialog dialog = QRCodeDialog.newInstance(true, meetingInfo);
		Fragment fragment = getFragmentManager().findFragmentByTag(TAG_DIALOG_QRCODE);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (fragment != null)
			ft.remove(fragment);
		ft.addToBackStack(null);
		dialog.show(ft, TAG_DIALOG_QRCODE);
	}

	/**
	 * 跳转到主题会议详情界面
	 */
	@Override
	public void skipToThemeDetailActivity(MeetingUserInfo info) {
		Intent intent = new Intent(this, ThemeDetailActivity.class);
		intent.putExtra(IntentExtra.MEETING_USER_INFO, info);
		startActivity(intent);
	}

	/**
	 * 跳转到头脑风暴详情界面
	 */
	@Override
	public void skipToBrainStormActivity(MeetingUserInfo info) {

	}

	/**
	 * 跳转到登录界面
	 */
	@Override
	public void skipToLoginActivity() {
		finish();
	}

	@Override
	public void skipToSearchActivity() {
		startActivity(new Intent(this, SearchMeetingActivity.class));
	}

	@Override
	public void skipToScanQRCodeActivity() {
		Intent intent = new Intent(this, CaptureActivity.class);

		startActivityForResult(intent, REQUEST_QR_CODE);
	}

	@Override
	public void skipToCreateMeetingActivity() {
		startActivityForResult(new Intent(this, ThemeMeetingCreateActivity.class), REQUEST_CREATE_MEETING_CODE);
	}

	// 监听返回键
	@Override
	public void onBackPressed() {
		LogUtils.i(TAG, "--点击返回键--");
		// 让应用回到桌面
//		moveTaskToBack(true);
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 2000) {
			ToastUtils.getInstance().showToast(this, R.string.prompt_double_click_exit);
			firstTime = secondTime;
		} else {
//			finish();
			ActivityManager.getInstance().finishAll();
		}
	}

}
