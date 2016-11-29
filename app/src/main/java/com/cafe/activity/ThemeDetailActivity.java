package com.cafe.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.cafe.common.mvp.MVPActivity;
import com.cafe.contract.ThemeDetailContract;
import com.cafe.data.meeting.GetNowTalkerResponse;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.ProcedureInfo;
import com.cafe.data.meeting.SpeakType;
import com.cafe.presenter.ThemeDetailPresenter;
import com.cafe.view.ChronometerAsc;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.TimeUtils;
import org.justin.utils.system.DisplayUtils;
import org.justin.utils.thread.ThreadUtils;

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

	private ImageView shakePhoneIv;
	private CircleImageView speakerPortraitCiv;
	private TextView speakerNameTv;
	private TextView speakerStateTv;
	private RecyclerView procedureListRv;

	private ProcedureListRvAdapter procedureListRvAdapter;

	private MeetingUserInfo meetingInfo;

	/**
	 * 定时任务，用户轮询当前说话人和说话详情列表
	 */
	private Timer timer;
	private TimerTask timerTask;


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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 一定要结束计时器，避免内存泄露
		if (meetingTimeCasc.isTimeStart())
			meetingTimeCasc.stopTime();
		if (timerTask != null)
			timerTask.cancel();
		if (timer != null)
			timer.cancel();
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
		shakePhoneIv = (ImageView) findViewById(R.id.shake_phone_iv);
		meetingTimeCasc = (ChronometerAsc) findViewById(R.id.meeting_time_casc);
		speakerPortraitCiv = (CircleImageView) findViewById(R.id.talker_portrait_civ);
		speakerNameTv = (TextView) findViewById(R.id.speaker_name_tv);
		speakerStateTv = (TextView) findViewById(R.id.speaker_state_tv);
		procedureListRv = (RecyclerView) findViewById(R.id.procedure_list_rv);
		// TODO:还差一个倒计时
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
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		procedureListRv.setLayoutManager(layoutManager);
		// 加入item动画效果
		SlideInDownAnimator animator = new SlideInDownAnimator();
		animator.setAddDuration(500);
		animator.setInterpolator(new BounceInterpolator());
		procedureListRv.setItemAnimator(animator);
	}

	// TODO:轮询当前说话人和会议过程列表
	private void getProcedureInfo() {
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				getPresenter().getNowTalker(meetingInfo);
				getPresenter().loadProcedureList(meetingInfo);
			}
		};
		timer.schedule(timerTask, 500, QUERY_PROCEDURE_PERIOD);
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
	public void refreshAfterStartEpisode() {
		meetingTimeCasc.setVisibility(View.GONE);
	}

	@Override
	public void refreshAfterStopEpisode() {
		meetingTimeCasc.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置当前说话人信息
	 */
	@Override
	public void setNowTalker(GetNowTalkerResponse.GetNowTalkerResult result) {
		speakerPortraitCiv = (CircleImageView) findViewById(R.id.talker_portrait_civ);
		speakerNameTv = (TextView) findViewById(R.id.speaker_name_tv);
		speakerStateTv = (TextView) findViewById(R.id.speaker_state_tv);
		speakerNameTv.setText(result.userName);
		speakerStateTv.setText(getSpeakType(result.type));
		// 加载说话人头像
		ImageLoader.getInstance().displayImage(result.userPortrait,
				speakerPortraitCiv, CommonUtils.getPortraitOptions(),
				new AnimationImageLoadingListener());
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
		return getString(R.string.prompt_talking);
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
			procedureListRvAdapter.add(i, procedureInfos.get(i));
		}
	}
}
