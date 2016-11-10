package com.ai2020lab.cafe.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ai2020lab.aiutils.common.ResourcesUtils;
import com.ai2020lab.aiutils.system.DisplayUtils;
import com.ai2020lab.aiviews.imageview.RoundImageView;
import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.common.mvp.MVPActivity;
import com.ai2020lab.cafe.contract.MeetingListContract;
import com.ai2020lab.cafe.presenter.MeetingListPresenter;

/**
 * 会议列表界面
 * Created by Justin Z on 2016/11/7.
 * 502953057@qq.com
 */

public class MeetingListActivity extends MVPActivity<MeetingListContract.View,
		MeetingListPresenter> implements MeetingListContract.View, View.OnClickListener {

	private final static String TAG = MeetingListActivity.class.getSimpleName();


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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_list);
		setToolbar();
		initViews();
		setUserInfo();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.theme_meeting_fab:
				// 创建主题会议

				break;
			case R.id.brain_storm_fab:
				// 提示摇一摇手机创建头脑风暴
				promptShakePhone();
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
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) headerView.getLayoutParams();
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
		meetingListRv = (RecyclerView) findViewById(R.id.meeting_list_Rv);
	}

	/**
	 * 设置工具栏
	 */
	private void setToolbar() {
		supportToolbar(true);
		setToolbarLeft(R.mipmap.logout, new OnLeftClickListener() {
			@Override
			public void onClick() {
				getPresenter().logout();
			}
		});
		setToolbarRight1(R.mipmap.search, new OnRightClickListener() {
			@Override
			public void onClick() {
				getPresenter().search();
			}
		});
		setToolbarRight2(R.mipmap.scan, new OnRightClickListener() {
			@Override
			public void onClick() {
				getPresenter().scanQRCode();
			}
		});
	}


	@Override
	public void showMeetingList() {

	}

	/**
	 * 设置用户信息显示
	 */
	@Override
	public void setUserInfo() {
		userPortraitIv.setImageDrawable(ResourcesUtils.getDrawable(R.mipmap.user1));
		userNameTv.setText("乔妈妈");
		workNumberTv.setText("工号：27420");
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
	 * 跳转到会议详情界面
	 */
	@Override
	public void skipToMeetingDetailActivity() {

	}


}
