package com.ai2020lab.cafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ai2020lab.aiutils.common.ResourcesUtils;
import com.ai2020lab.aiutils.system.DeviceUtils;
import com.ai2020lab.aiutils.system.DisplayUtils;
import com.ai2020lab.aiviews.imageview.RoundImageView;
import com.ai2020lab.aiviews.textview.ImageTextButton;
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
		MeetingListPresenter> implements MeetingListContract.View {

	private final static String TAG = MeetingListActivity.class.getSimpleName();

	private View headerView;

	private RoundImageView userPortraitIv;
	private TextView userNameTv;
	private TextView workNumberTv;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_list);
		setToolbar();
		initViews();
		getPresenter().setUserInfo();
	}

	@Override
	public MeetingListPresenter initPresenter() {
		return new MeetingListPresenter(this);
	}


	private void initViews() {
		headerView = findViewById(R.id.meeting_list_header);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) headerView.getLayoutParams();
		lp.height = DisplayUtils.getScreenHeight(this) / 4;
		headerView.setLayoutParams(lp);
		userPortraitIv = (RoundImageView) findViewById(R.id.user_portrait_iv);
		userNameTv = (TextView) findViewById(R.id.user_name_tv);
		workNumberTv = (TextView) findViewById(R.id.work_number_tv);
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
	 * 跳转到会议详情界面
	 */
	@Override
	public void skipToMeetingDetailActivity() {

	}
}
