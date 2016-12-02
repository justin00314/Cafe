package com.cafe.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import org.justin.utils.common.TimeUtils;
import org.justin.utils.system.DisplayUtils;
import org.justin.utils.thread.ThreadUtils;

import java.lang.ref.WeakReference;
import java.util.Date;
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
	private final static int MSG_CHECK_START_TIME = 0x0030;

	private final static int COUNT_DOWN_MAX = 30;

	/**
	 * 每5秒轮询一次会议过程和当前说话人
	 */
	private final static int QUERY_PROCEDURE_PERIOD = 1000;

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

	/**
	 * 定时任务，用户轮询当前说话人和说话详情列表
	 */
	private Timer timer;
	private TimerTask timerTask;

	/**
	 * 手势监听
	 */
	private GestureDetector gestureDetector;


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
		setProcedureListRv();
		setMeetingInfo();
		setMeetingCasc();
		// 轮询
		getProcedureInfo();
		// 监听手势
		listenGesture();
		// 摇一摇监听
		startShakePhone();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopShakePhone();
		// 一定要结束计时器，避免内存泄露
		if (meetingTimeCasc.isTimeStart())
			meetingTimeCasc.stopTime();
		if (meetingTimeCdesc.isTimeStart())
			meetingTimeCdesc.stopTime();
		if (timerTask != null)
			timerTask.cancel();
		if (timer != null)
			timer.cancel();
		// 退出界面需要清除过滤条件的时间
		PreManager.setProcedureFilterTime(this, "");
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return gestureDetector.onTouchEvent(e);
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
		speakerPortraitCiv = (CircleImageView) findViewById(R.id.speaker_portrait_civ);
		speakerNameTv = (TextView) findViewById(R.id.speaker_name_tv);
		speakerStateTv = (TextView) findViewById(R.id.speaker_state_tv);
		procedureListRv = (RecyclerView) findViewById(R.id.procedure_list_rv);
		// TODO:还差一个倒计时
	}

	// TODO:监听手势，双击屏幕开始主题,还需要完善双指敲击
	private void listenGesture() {
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			// 监听双击事件
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				getPresenter().operateTheme(meetingInfo);
				return super.onDoubleTap(e);
			}
		});
	}

	private void startShakePhone() {
		ShakePhoneUtils.getInstance().startShake(this, new ShakePhoneUtils.OnShakeListener() {
			@Override
			public void onShake() {
				LogUtils.i(TAG, "-->手机摇一摇");
				// 开始插话
				getPresenter().operateEpisode(meetingInfo);
			}
		});
	}

	private void stopShakePhone() {
		ShakePhoneUtils.getInstance().stopShake();
	}

	private void setMeetingInfo() {
		meetingInfo = (MeetingUserInfo) getIntent().getSerializableExtra(
				IntentExtra.MEETING_USER_INFO);
		meetingNameTv.setText(meetingInfo.name);
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
				handler.obtainMessage(MSG_CHECK_START_TIME).sendToTarget();
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
				case MSG_CHECK_START_TIME:
					activity.startTime();
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

	private void startTime() {
		// TODO:根据当前时间和会议开始时间计算计时器的初始值
		long startTime = TimeUtils.dateToTimeStamp(meetingInfo.startTime,
				TimeUtils.Template.YMDHMS) / 1000;
		long currentTime = new Date().getTime() / 1000;
		LogUtils.i(TAG, "会议开始时间-->" + startTime);
		LogUtils.i(TAG, "当前时间-->" + currentTime);
		long base = currentTime - startTime;
		meetingTimeCasc.setCurrentTime(base > 0 ? base : 0);
		if (base >= 0) {
			LogUtils.i(TAG, "会议已经开始了-->" + base + " 秒");
			meetingTimeCasc.startTime();
			meetingStateTv.setText(getString(R.string.meeting_state_progress_));
		}
		// 会议还没有开始
		else {
			meetingStateTv.setText(getString(R.string.meeting_state_not_start));
		}
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
		meetingTimeCdesc.setCurrentTime(0);
		meetingTimeCdesc.stopTime();
	}

	/**
	 * 设置当前说话人信息
	 */
	@Override
	public void setNowTalker(GetNowTalkerResponse.GetNowTalkerResult result) {
		speakerNameTv.setText(TextUtils.isEmpty(result.userName) ? "" :
				result.userName);
		speakerStateTv.setText(getSpeakType(result.type));
		// 加载说话人头像
		ImageLoader.getInstance().displayImage(result.userPortrait,
				speakerPortraitCiv, CommonUtils.getPortraitOptions(),
				new AnimationImageLoadingListener());
//		if (result.type == SpeakType.EPISODE && !meetingTimeCdesc.isTimeStart()) {
//			refreshAfterStartEpisode();
//		}
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

	/**
	 * 跳转到登录界面
	 */
	@Override
	public void skipToLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
