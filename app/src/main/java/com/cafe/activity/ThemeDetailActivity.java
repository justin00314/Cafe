package com.cafe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiviews.anim.AnimationImageLoadingListener;
import com.aiviews.toolbar.ToolbarActivity;
import com.cafe.R;
import com.cafe.adapter.ProcedureListRvAdapter;
import com.cafe.common.CommonUtils;
import com.cafe.common.IntentAction;
import com.cafe.common.IntentExtra;
import com.cafe.common.PreManager;
import com.cafe.common.ShakePhoneUtils;
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.ThemeDetailContract;
import com.cafe.data.meeting.GetNowTalkerResponse;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.ProcedureInfo;
import com.cafe.data.meeting.SpeakType;
import com.cafe.presenter.ThemeDetailPresenter;
import com.cafe.view.ChronometerAsc;
import com.cafe.view.ChronometerDesc;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.justin.utils.common.LogUtils;
import org.justin.utils.system.DisplayUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * 会议详情界面
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class ThemeDetailActivity extends MVPActivity<ThemeDetailContract.View,
		ThemeDetailPresenter> implements ThemeDetailContract.View, View.OnClickListener {

	private final static String TAG = ThemeDetailActivity.class.getSimpleName();

	private final static int MSG_GET_SPEAKER = 0x0010;
	private final static int MSG_GET_PROCEDURE = 0x0020;
//	private final static int MSG_CHECK_START_TIME = 0x0030;

	private final static int COUNT_DOWN_MAX = 30;

	/**
	 * 每5秒轮询一次会议过程和当前说话人
	 */
	private final static int QUERY_PROCEDURE_PERIOD = 1000 * 5;

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
	 * 倒计时
	 */
	private ChronometerDesc meetingTimeCdesc;

	private ImageView shakePhoneIv;
	private CircleImageView speakerPortraitCiv;
	private TextView speakerNameTv;
	private TextView speakerStateTv;
	private RecyclerView procedureListRv;

	private ProcedureListRvAdapter procedureListRvAdapter;
	private LinearLayoutManager layoutManager;

	private MeetingUserInfo meetingInfo;
	private String nowTalkerPortrait = "";

	/**
	 * 定时任务，用户轮询当前说话人和说话详情列表
	 */
	private Timer timer;
	private TimerTask timerTask;

	/**
	 * 手势监听
	 */
	private GestureDetector gestureDetector;

	private PresentAtMeetingReceiver receiver;

	/**
	 * 用户控制手机是否能点击，用于控制短时间触屏2次
	 */
	private boolean isTap = false;
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
		setContentView(R.layout.activity_meeting_theme);
		setToolbar();
		// 初始化各个view
		initViews();
		// 设置会议基本信息
		setMeetingInfo();
		setProcedureListRv();
		// 轮询说话详情列表
		getProcedureInfo();
		setMeetingCasc();
		// 开始会议计时
		getPresenter().startMeetingTime(meetingInfo);
		// 监听手势
		listenGesture();
		// 开始手机摇一摇监听
		startShake();
		// 注册广播监听用户再会状态
		registerReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 退出界面需要清除过滤条件的时间
		PreManager.setProcedureFilterTime(this, "");
		// 结束监听手机摇一摇
		stopShake();
		// 一定要结束计时器，避免内存泄露
		getPresenter().stopMeetingTime();
		if (meetingTimeCdesc.isTimeStart())
			meetingTimeCdesc.stopTime();
		if (timerTask != null)
			timerTask.cancel();
		if (timer != null)
			timer.cancel();
		unregisterReceiver();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return gestureDetector.onTouchEvent(e);
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
		shakePhoneIv = (ImageView) findViewById(R.id.shake_phone_iv);
		meetingTimeCasc = (ChronometerAsc) findViewById(R.id.meeting_time_casc);
		meetingTimeCdesc = (ChronometerDesc) findViewById(R.id.meeting_time_cdesc);
		speakerPortraitCiv = (CircleImageView) findViewById(R.id.speaker_portrait_civ);
		speakerNameTv = (TextView) findViewById(R.id.speaker_name_tv);
		speakerStateTv = (TextView) findViewById(R.id.speaker_state_tv);
		procedureListRv = (RecyclerView) findViewById(R.id.procedure_list_rv);
	}

	// TODO:监听手势，双击屏幕开始主题,还需要完善双指敲击
	private void listenGesture() {
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			// 监听双击事件
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// 避免多次点击
				if(!isTap)
					getPresenter().operateTheme(meetingInfo);
				return super.onDoubleTap(e);
			}
		});
	}

	/**
	 * 设置会议信息
	 */
	private void setMeetingInfo() {
		meetingNameTv.setText(meetingInfo.name);
		meetingStateTv.setText(getString(R.string.meeting_state_progress_));
	}

	/**
	 * 设置会议说话列表
	 */
	private void setProcedureListRv() {
		procedureListRvAdapter = new ProcedureListRvAdapter(this);
		layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		procedureListRv.setLayoutManager(layoutManager);
		procedureListRv.setAdapter(procedureListRvAdapter);
		// 加入item动画效果
		SlideInDownAnimator animator = new SlideInDownAnimator();
		animator.setAddDuration(500);
		animator.setInterpolator(new BounceInterpolator());
		procedureListRv.setItemAnimator(animator);
	}

	// TODO:轮询当前说话人和会议过程列表
	private void getProcedureInfo() {
		final RefreshHandler handler = new RefreshHandler(this, meetingInfo);
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				handler.obtainMessage(MSG_GET_SPEAKER).sendToTarget();
				handler.obtainMessage(MSG_GET_PROCEDURE).sendToTarget();
			}
		};
		timer.schedule(timerTask, 500, QUERY_PROCEDURE_PERIOD);
	}

	private static class RefreshHandler extends Handler {

		private WeakReference<ThemeDetailActivity> reference;
		private MeetingUserInfo meetingInfo;

		RefreshHandler(ThemeDetailActivity activity, MeetingUserInfo info) {
			reference = new WeakReference<>(activity);
			this.meetingInfo = info;
		}

		@Override
		public void handleMessage(Message msg) {
			ThemeDetailActivity activity = reference.get();
			if (activity == null) return;
			switch (msg.what) {
				case MSG_GET_SPEAKER:
					activity.getPresenter().getNowTalker(meetingInfo);
					break;
				case MSG_GET_PROCEDURE:
					activity.getPresenter().loadProcedureList(meetingInfo);
					break;
			}

		}
	}

	/**
	 * 设置会议正向计时
	 */
	private void setMeetingCasc() {
		// 首先设置计时器的样式
		int size = DisplayUtils.getScreenWidth(this);
		meetingTimeCasc.setSize(size * 2 / 3);
		meetingTimeCdesc.setSize(size * 2 / 3);
	}

	@Override
	public void onClick(View view) {

	}

	@Override
	public ThemeDetailPresenter initPresenter() {
		return new ThemeDetailPresenter(this);
	}

	/**
	 * 开始监听摇晃手机
	 */
	@Override
	public void startShake() {
		ShakePhoneUtils.getInstance().startShake(this, new ShakePhoneUtils.OnShakeListener() {
			@Override
			public void onShake() {
//				LogUtils.i(TAG, "-->手机摇一摇");
				// 开始插话
				getPresenter().operateEpisode(meetingInfo);
			}
		});
		isStartShake = true;
	}

	/**
	 * 结束监听摇晃手机
	 */
	@Override
	public void stopShake() {
		ShakePhoneUtils.getInstance().stopShake();
		isStartShake = false;
	}

	@Override
	public boolean isTap() {
		return isTap;
	}

	@Override
	public void setIsTap(boolean isTap) {
		this.isTap = isTap;
	}

	@Override
	public boolean isStartShake() {
		return isStartShake;
	}


	/**
	 * 倒计时是否开始
	 */
	@Override
	public boolean isTimeDescStart() {
		return meetingTimeCdesc.isTimeStart();
	}

	/**
	 * 退出界面
	 */
	@Override
	public void finishActivity() {
		LogUtils.i(TAG, "--详情界面关闭--");
		finish();
	}

	/**
	 * 开始会议计时
	 */
	@Override
	public void startMeetingTime(long time) {
		if (meetingTimeCasc.isTimeStart()) return;
		meetingTimeCasc.setCurrentTime(time);
		meetingTimeCasc.startTime();
	}

	/**
	 * 结束会议计时
	 */
	@Override
	public void stopMeetingTime() {
		if (meetingTimeCasc.isTimeStart())
			meetingTimeCasc.stopTime();
	}

	/**
	 * 展示插话倒计时，这个时候正计时隐藏,插话结束之后隐藏倒计时并显示正计时
	 */
	@Override
	public void refreshAfterStartEpisode() {
		meetingTimeCasc.setVisibility(View.GONE);
		meetingTimeCdesc.setVisibility(View.VISIBLE);
		// 执行动画
//		ObjectAnimator scaleX = ObjectAnimator.ofFloat(meetingTimeCasc, "scaleX", 1f, 0f);
//		ObjectAnimator scaleY = ObjectAnimator.ofFloat(meetingTimeCasc, "scaleY", 1f, 0f);
//		ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(meetingTimeCdesc, "scaleX", 0f, 1f);
//		ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(meetingTimeCdesc, "scaleY", 0f, 1f);

		// 倒计时开始
		meetingTimeCdesc.setCurrentTime(COUNT_DOWN_MAX);
		meetingTimeCdesc.startTime();
	}

	@Override
	public void refreshAfterStopEpisode() {
		meetingTimeCasc.setVisibility(View.VISIBLE);
		meetingTimeCdesc.setVisibility(View.GONE);
		// 倒计时结束
		meetingTimeCdesc.stopTime();
	}

	/**
	 * 设置当前说话人信息
	 */
	@Override
	public void setNowTalker(GetNowTalkerResponse.GetNowTalkerResult result) {
		// 只有数据发生了变化才更新界面
		if (!speakerNameTv.getText().toString().equals(result.userName)) {
			speakerNameTv.setText(result.userName);
		}
		String speakerState = getSpeakType(result.speakType);
		if (!speakerStateTv.getText().toString().equals(speakerState)) {
			speakerStateTv.setText(speakerState);
		}
		String userPortrait = "";
		if(result.userPortrait != null) userPortrait = result.userPortrait;
		if (!nowTalkerPortrait.equals(userPortrait)) {
			// 加载说话人头像
			ImageLoader.getInstance().displayImage(result.userPortrait,
					speakerPortraitCiv, CommonUtils.getPortraitOptions(),
					new AnimationImageLoadingListener());
		}
		nowTalkerPortrait = userPortrait;

	}

	/**
	 * 获取说话类型文字展示
	 */
	private String getSpeakType(int type) {
		switch (type) {
			case SpeakType.THEME:
				return getString(R.string.prompt_talking);
			case SpeakType.EPISODE:
				return getString(R.string.prompt_episode);
		}
		return getString(R.string.prompt_no_speaker);
	}

	/**
	 * 加载说话列表数据
	 */
	@Override
	public void loadProcedureList(List<ProcedureInfo> procedureInfos) {
		if (procedureInfos == null || procedureInfos.size() == 0) {
			LogUtils.i(TAG, "-->没有说话记录列表数据");
			return;
		}
		int size = procedureInfos.size();
		for (int i = 0; i < size; i++) {
			// 将数据插入
			LogUtils.i(TAG, "说话详情数据插入-->" + procedureInfos.get(i).userName);
			procedureListRvAdapter.add(i, procedureInfos.get(i));
			layoutManager.scrollToPosition(0);
		}
	}

}
