package com.cafe.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiviews.toolbar.ToolbarActivity;
import com.cafe.R;
import com.cafe.common.IntentExtra;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.ThemeDetailContract;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.presenter.ThemeDetailPresenter;
import com.cafe.view.ChronometerAsc;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.TimeUtils;
import org.justin.utils.system.DisplayUtils;
import org.justin.utils.thread.ThreadUtils;

import java.util.Date;

/**
 * 会议详情界面
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class ThemeDetailActivity extends MVPActivity<ThemeDetailContract.View,
		ThemeDetailPresenter> implements ThemeDetailContract.View, View.OnClickListener {

	private final static String TAG = ThemeDetailActivity.class.getSimpleName();

	private TextView meetingNameTv;
	private TextView meetingStateTv;
	private ChronometerAsc meetingTimeCasc;

	private MeetingUserInfo meetingInfo;

	/**
	 * 界面入口
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置屏幕不休眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_meeting_theme);
		setToolbar();
		initViews();
//		setMeetingListRv();
		setMeetingInfo();
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
	}

	/**
	 * 设置会议正向计时
	 */
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
		long startTime = TimeUtils.dateToTimeStamp(meetingInfo.startTime, TimeUtils.Template.YMDHMS);
		long currentTime = new Date().getTime() / 1000;
		long base = currentTime - startTime;
		LogUtils.i(TAG, "会议已经开始了-->" + base + " 秒");
		meetingTimeCasc.setCurrentTime(base > 0 ? base : 0);
		meetingTimeCasc.startTime();
	}

	@Override
	public void onClick(View view) {

	}

	@Override
	public ThemeDetailPresenter initPresenter() {
		return new ThemeDetailPresenter(this);
	}

	/**
	 * 展示插话倒计时，这个时候正计时隐藏,插话结束之后隐藏倒计时并显示正计时
	 */
	@Override
	public void showEpisode(boolean flag) {
		if (flag) {
			meetingTimeCasc.setVisibility(View.GONE);
		} else {
			meetingTimeCasc.setVisibility(View.VISIBLE);
		}

	}
}
