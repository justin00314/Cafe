package com.cafe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.aiviews.toolbar.ToolbarActivity;
import com.cafe.R;
import com.cafe.adapter.ParticipantListRvAdapter;
import com.cafe.common.IntentAction;
import com.cafe.common.IntentExtra;
import com.cafe.common.ShakePhoneUtils;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.BrainStormDetailContract;
import com.cafe.data.account.UserInfo;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.presenter.BrainStormDetailPresenter;
import com.cafe.view.ChronometerAsc;

import org.justin.utils.common.LogUtils;
import org.justin.utils.system.DisplayUtils;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * 头脑风暴详情界面
 * Created by Justin Z on 2016/12/6.
 * 502953057@qq.com
 */

public class BrainStormDetailActivity extends MVPActivity<BrainStormDetailContract.View,
		BrainStormDetailPresenter> implements BrainStormDetailContract.View {

	private final static String TAG = BrainStormDetailActivity.class.getSimpleName();

	/**
	 * 会议名字
	 */
	private TextView meetingNameTv;
	/**
	 * 会议状态
	 */
	private TextView meetingStateTv;
	/**
	 * 正向计时
	 */
	private ChronometerAsc meetingTimeCasc;

	/**
	 * 用户在会状态广播接收器
	 */
	private PresentAtMeetingReceiver receiver;

	private RecyclerView participantListRv;
	private ParticipantListRvAdapter participantListRvAdapter;
	private LinearLayoutManager layoutManager;

	private MeetingUserInfo meetingInfo;

	/**
	 * 用户控制手机是否能摇晃--解决短时间摇晃2次
	 */
	private boolean isStartShake = false;

	/**
	 * 界面入口
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置屏幕不休眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		meetingInfo = (MeetingUserInfo) getIntent().getSerializableExtra(
				IntentExtra.MEETING_USER_INFO);
		setContentView(R.layout.activity_meeting_brain_storm);
		setToolbar();
		// 初始化各个view
		initViews();
		// 设置会议基本信息
		setMeetingInfo();
		// 设置会议计时器样式
		setMeetingCasc();
		// 设置会议参与者列表
		setParticipantListRv();
		// 注册广播监听用户在会状态
		registerReceiver();
		// 开始手机摇一摇监听
		startShake();
		// 加载会议参与者列表
		getPresenter().loadMeetingParticipantList(meetingInfo);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 结束监听手机摇一摇
		stopShake();
		// 一定要结束计时器，避免内存泄露
		getPresenter().stopMeetingTime();
		unregisterReceiver();
	}

	// 注册用户在会状态广播
	private void registerReceiver() {
		if (receiver == null)
			receiver = new PresentAtMeetingReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(IntentAction.RECEIVER_IS_PRESENT_AT_MEETING);
		registerReceiver(receiver, intentFilter);
	}

	/**
	 * 反注册用户在会状态广播
	 */
	private void unregisterReceiver() {
		if (receiver != null)
			unregisterReceiver(receiver);

	}

	/**
	 * 用户在会状态广播接收器
	 */
	private class PresentAtMeetingReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			MeetingUserInfo info = (MeetingUserInfo) intent
					.getSerializableExtra(IntentExtra.MEETING_USER_INFO);
			getPresenter().doReceiveAtMeeting(info);
		}
	}

	/**
	 * 设置工具栏
	 */
	private void setToolbar() {
		supportToolbar(true);
		setToolbarTitle(getString(R.string.activity_title_meeting_detail));
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
		participantListRv = (RecyclerView) findViewById(R.id.participant_list_rv);
	}

	/**
	 * 设置会议信息
	 */
	private void setMeetingInfo() {
		meetingNameTv.setText(getString(R.string.brain_storm));
		meetingStateTv.setText(getString(R.string.meeting_state_progress_));
	}

	/**
	 * 设置会议正向计时
	 */
	private void setMeetingCasc() {
		// 首先设置计时器的样式
		int size = DisplayUtils.getScreenWidth(this);
		meetingTimeCasc.setSize(size * 2 / 3);
	}

	/**
	 * 设置会议说话列表
	 */
	private void setParticipantListRv() {
		participantListRvAdapter = new ParticipantListRvAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		participantListRv.setLayoutManager(layoutManager);
		participantListRv.setAdapter(participantListRvAdapter);
		// 加入item动画效果
		SlideInDownAnimator animator = new SlideInDownAnimator();
		animator.setAddDuration(500);
		animator.setInterpolator(new BounceInterpolator());
		participantListRv.setItemAnimator(animator);
	}

	@Override
	public BrainStormDetailPresenter initPresenter() {
		return new BrainStormDetailPresenter(this);
	}

	/**
	 * 开始监听手机摇一摇
	 */
	@Override
	public void startShake() {
		ShakePhoneUtils.getInstance().startShake(this,
				new ShakePhoneUtils.OnShakeListener() {
					@Override
					public void onShake() {
						// 摇一摇
						getPresenter().shakePhoneForBrainStorm();
					}
				});
		isStartShake = true;
	}

	/**
	 * 停止监听手机摇一摇
	 */
	@Override
	public void stopShake() {
		ShakePhoneUtils.getInstance().stopShake();
		isStartShake = false;
	}

	@Override
	public boolean isStartShake() {
		return isStartShake;
	}

	@Override
	public boolean isTimeDescStart() {
		return meetingTimeCasc.isTimeStart();
	}

	@Override
	public void finishActivity() {
		finish();
	}

	@Override
	public void startMeetingTime(long time) {
		if (meetingTimeCasc.isTimeStart()) return;
		meetingTimeCasc.setCurrentTime(time);
		meetingTimeCasc.startTime();
	}

	@Override
	public void stopMeetingTime() {
		if (meetingTimeCasc.isTimeStart())
			meetingTimeCasc.stopTime();
	}

	/**
	 * 加载参会人列表
	 */
	@Override
	public void loadParticipantList(List<UserInfo> userInfos) {
		int size = userInfos.size();
		for (int i = 0; i < size; i++) {
			// 将数据插入
			LogUtils.i(TAG, "参与者数据插入-->" + userInfos.get(i).userName);
			participantListRvAdapter.add(i, userInfos.get(i));
			layoutManager.scrollToPosition(0);
		}
	}


}
