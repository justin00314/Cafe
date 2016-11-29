package com.cafe.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cafe.R;
import com.cafe.common.CommonUtils;

import org.justin.utils.system.DisplayUtils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 正向计时器
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class ChronometerAsc extends RelativeLayout {


	private final static String TAG = ChronometerAsc.class.getSimpleName();

	private final static int MSG_REFRESH = 0x200;

	private TextView showTimeTv;
	private ImageView showLoadingIv;

	private long currentTime;

	private Timer timer;
	private TimerTask timerTask;

	private ObjectAnimator animator;

	private boolean isAnimStart = false;

	private Context context;

	public ChronometerAsc(Context context) {
		super(context);
		init(context);
	}

	public ChronometerAsc(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ChronometerAsc(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.chronometer_ascend, this, true);
		showTimeTv = (TextView) findViewById(R.id.show_time_tv);
		showLoadingIv = (ImageView) findViewById(R.id.show_loading_iv);
	}

	/**
	 * 开始执行旋转动画
	 */
	private void startRotationAnim() {
		if (animator == null)
			animator = ObjectAnimator.ofFloat(showLoadingIv, "rotation", 0f, 360f);
		animator.setDuration(1000);
		// 一直不停的转
		animator.setRepeatCount(ObjectAnimator.INFINITE);
		animator.setInterpolator(new LinearInterpolator());
//		showLoadingIv.setPivotX(showLoadingIv.getWidth() / 2);
//		showLoadingIv.setPivotY(showLoadingIv.getHeight() / 2);
		animator.start();
		isAnimStart = true;
	}

	/**
	 * 停止执行旋转动画
	 */
	private void stopRotationAnim() {
		if (animator != null) {
			animator.cancel();
			animator = null;
		}
		isAnimStart = false;
	}

	public boolean isTimeStart() {
		return isAnimStart;
	}

	/**
	 * 设置计时器的大小
	 *
	 * @param size 计时器大小，单位为像素
	 */
	public void setSize(int size) {
		RelativeLayout.LayoutParams lp = (LayoutParams) showLoadingIv.getLayoutParams();
		lp.width = size;
		lp.height = size;
		showLoadingIv.setLayoutParams(lp);
		// 计算字体大小，字体和loading图的比例关系为1:7
		int sizeDp = DisplayUtils.pxToDpInt(context, size);
		showTimeTv.setTextSize(sizeDp / 7);
	}

	/**
	 * 设置计时器当前时间
	 *
	 * @param time 单位为秒
	 */
	public void setCurrentTime(long time) {
		this.currentTime = time;
		String hour = CommonUtils.getNumberString(CommonUtils.getHour(time));
		String minute = CommonUtils.getNumberString(CommonUtils.getMinute(time));
		String second = CommonUtils.getNumberString(CommonUtils.getSecond(time));
		String timeStr = String.format(context.getString(R.string.hour_min_second),
				hour, minute, second);
		showTimeTv.setText(timeStr);
	}

	/**
	 * 获取计时器当前时间，单位为秒
	 *
	 * @return 返回当前时间，单位为秒
	 */
	public long getCurrentTime() {
		return currentTime;
	}

	/**
	 * 开始计时,每秒加1
	 */
	public void startTime() {
		final RefreshHandler handler = new RefreshHandler(this);
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				currentTime++;
				handler.obtainMessage(MSG_REFRESH, currentTime).sendToTarget();
			}
		};
		timer.schedule(timerTask, 0, 1000);
		// 开始执行旋转动画
		startRotationAnim();
	}

	/**
	 * 停止计时,在Activity的 onDestroy 中调用
	 */
	public void stopTime() {
		if (timerTask != null)
			timerTask.cancel();
		if (timer != null)
			timer.cancel();
		// 停止执行旋转动画
		stopRotationAnim();
	}

	private static class RefreshHandler extends Handler {

		private WeakReference<ChronometerAsc> cascReference;

		RefreshHandler(ChronometerAsc casc) {
			cascReference = new WeakReference<>(casc);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_REFRESH:
					ChronometerAsc casc = cascReference.get();
					if(casc == null) return;
					casc.setCurrentTime((long) msg.obj);
					break;
			}

		}
	}


}
