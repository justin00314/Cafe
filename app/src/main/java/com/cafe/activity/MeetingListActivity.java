package com.cafe.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.widget.Toast;

import com.aiviews.anim.AnimationImageLoadingListener;
import com.aiviews.imageview.RoundImageView;
import com.aiviews.toolbar.ToolbarActivity;
import com.cafe.R;
import com.cafe.adapter.MeetingListRvAdapter;
import com.cafe.common.ImageLoaderManager;
import com.cafe.common.IntentExtra;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.UserInfo;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.fragment.QRCodeDialog;
import com.cafe.presenter.MeetingListPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ResourcesUtils;
import org.justin.utils.system.DisplayUtils;

import java.util.List;

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
	 * 创建主题会议FAB
	 */
	private FloatingActionButton createThemeMeetingFab;
	/**
	 * 创建头脑风暴FAB
	 */
	private FloatingActionButton createBrainStormFab;

	private RecyclerView meetingListRv;
	private MeetingListRvAdapter meetingListRvAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_list);
		setToolbar();
		initViews();
		setMeetingListRv();
		// 获取用户信息
		getPresenter().getUserInfo();
		checkCameraPermission();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO:简单处理：每次进入界面都重新加载会议列表
		getPresenter().loadMeetingList();
//		getPresenter().loadMeetingListTest();
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

					//// TODO: 2016/11/30 scan QR success

				} else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

					//// TODO: 2016/11/30 SCAN QR fail
				//	Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
				}
			}
		}  else if (requestCode == REQUEST_CREATE_MEETING_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				//// TODO: 2016/11/30 create meeting success
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
		createThemeMeetingFab = (FloatingActionButton) findViewById(R.id.theme_meeting_fab);
		createBrainStormFab = (FloatingActionButton) findViewById(R.id.brain_storm_fab);
		createThemeMeetingFab.setOnClickListener(this);
		createBrainStormFab.setOnClickListener(this);
		meetingListRv = (RecyclerView) findViewById(R.id.meeting_list_rv);
	}

	/**
	 * 初始化会议列表
	 */
	private void setMeetingListRv() {
		meetingListRvAdapter = new MeetingListRvAdapter(this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
		MeetingUserInfo meetingInfo = meetingListRvAdapter.getItem(position);
		if (!meetingInfo.participatedFlag) {
			meetingInfo.participatedFlag = true;
			meetingListRvAdapter.notifyItemChanged(position);
		}
	}

	/**
	 * 退出成功之后刷新
	 */
	@Override
	public void refreshAfterQuit(MeetingUserInfo info) {
		int position = meetingListRvAdapter.getIndex(info.id);
		MeetingUserInfo meetingInfo = meetingListRvAdapter.getItem(position);
		if (meetingInfo.participatedFlag) {
			meetingInfo.participatedFlag = false;
			meetingListRvAdapter.notifyItemChanged(position);
		}
	}

	/**
	 * 取消成功之后刷新
	 */
	@Override
	public void refreshAfterCancel(MeetingUserInfo info) {
		meetingListRvAdapter.remove(meetingListRvAdapter.getIndex(info.id));
	}

	/**
	 * 解散成功之后刷新
	 */
	@Override
	public void refreshAfterDismiss(MeetingUserInfo info) {

	}

	/**
	 * 加载会议列表
	 */
	@Override
	public void loadMeetingList(List<MeetingUserInfo> meetingInfos) {
		meetingListRvAdapter.clear();
		if (meetingInfos == null || meetingInfos.size() == 0) {
			LogUtils.i(TAG, "-->没有会议列表数据");
			return;
		}
		int size = meetingInfos.size();
		for (int i = 0; i < size; i++) {
			LogUtils.i(TAG, "加入数据-->" + i);
			meetingListRvAdapter.add(i, meetingInfos.get(i));
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
		moveTaskToBack(true);
	}
}
