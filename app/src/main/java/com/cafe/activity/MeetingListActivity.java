package com.cafe.activity;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.cafe.common.ImageLoaderManager;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.account.UserInfo;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.fragment.QRCodeDialog;
import com.cafe.presenter.MeetingListPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ResourcesUtils;
import org.justin.utils.system.DisplayUtils;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * 会议列表界面
 * Created by Justin Z on 2016/11/7.
 * 502953057@qq.com
 */

public class MeetingListActivity extends MVPActivity<MeetingListContract.View,
		MeetingListPresenter> implements MeetingListContract.View, View.OnClickListener {

	private final static String TAG = MeetingListActivity.class.getSimpleName();

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO:简单处理：每次进入界面都重新加载会议列表
//		getPresenter().loadMeetingList();
		getPresenter().loadMeetingListTest();
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
	public MeetingListPresenter initPresenter() {
		return new MeetingListPresenter(this);
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
	 * 跳转到会议详情界面
	 */
	@Override
	public void skipToMeetingDetailActivity() {

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
//		startActivity(new Intent(this, SearchMeetingActivity.class));
	}

	@Override
	public void skipToCreateMeetingActivity() {
		startActivity(new Intent(this, ThemeMeetingCreateActivity.class));
	}

	// 监听返回键
	@Override
	public void onBackPressed() {
		LogUtils.i(TAG, "--点击返回键--");
		// 让应用回到桌面
		moveTaskToBack(true);
	}
}
