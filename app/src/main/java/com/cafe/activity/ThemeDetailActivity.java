package com.cafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aiviews.toolbar.ToolbarActivity;
import com.cafe.R;
import com.cafe.common.IntentExtra;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.MeetingDetailContract;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.presenter.MeetingDetailPresenter;
import com.cafe.view.ChronometerAsc;

import org.justin.utils.system.DisplayUtils;
import org.justin.utils.thread.ThreadUtils;

/**
 * 会议详情界面
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class ThemeDetailActivity extends MVPActivity<MeetingDetailContract.View,
		MeetingDetailPresenter> implements MeetingDetailContract.View, View.OnClickListener {

	private final static String TAG = ThemeDetailActivity.class.getSimpleName();

	private TextView meetingNameTv;
	private TextView meetingStateTv;
	private ChronometerAsc meetingTimeCasc;

	private MeetingUserInfo meetingInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_detail);
		setToolbar();
		initViews();
//		setMeetingListRv();
//		setMeetingInfo();
		setMeetingCasc();


	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (meetingTimeCasc.isTimeStart())
			meetingTimeCasc.stopTime();
	}

	/**
	 * 设置工具栏
	 */
	private void setToolbar() {
		supportToolbar(true);
//		setToolbarTitle();
		setToolbarLeft(R.mipmap.back, new ToolbarActivity.OnLeftClickListener() {
			@Override
			public void onClick() {
				finish();
			}
		});

	}

	private void initViews() {
		meetingNameTv = (TextView) findViewById(R.id.meeting_name_tv);
		meetingStateTv = (TextView) findViewById(R.id.meeting_state_tv);
		meetingTimeCasc = (ChronometerAsc) findViewById(R.id.meeting_time_casc);
	}

	private void setMeetingInfo() {
		meetingInfo = (MeetingUserInfo) getIntent().getSerializableExtra(
				IntentExtra.MEETING_USER_INFO);
		meetingNameTv.setText(meetingInfo.name);
		meetingStateTv.setText(meetingInfo.state);
	}

	private void setMeetingCasc() {
		// 首先设置计时器的样式
		int size = DisplayUtils.getScreenWidth(this);
		meetingTimeCasc.setSize(size * 3 / 4);
		// 延迟启动计时
		ThreadUtils.runOnUIThread(new Runnable() {
			@Override
			public void run() {

				startTime();
			}
		}, 500);


	}

	private void startTime() {
		// TODO:根据当前时间和会议开始时间计算计时器的初始值
		meetingTimeCasc.setCurrentTime(0);
		meetingTimeCasc.startTime();
	}

	@Override
	public void onClick(View view) {

	}

	@Override
	public MeetingDetailPresenter initPresenter() {
		return new MeetingDetailPresenter(this);
	}

	@Override
	public void setMeetingInfo(MeetingUserInfo info) {

	}
}
