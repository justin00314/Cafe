package com.cafe.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cafe.R;
import com.cafe.common.CommonUtils;

import org.justin.utils.common.ResourcesUtils;
import org.justin.utils.system.DisplayUtils;
import org.justin.utils.thread.ThreadUtils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 倒计时
 * Created by Justin Z on 2016/12/2.
 * 502953057@qq.com
 */

public class ChronometerDesc extends RelativeLayout {
	private final static String TAG = ChronometerAsc.class.getSimpleName();

	private final static int MSG_REFRESH = 0x200;

	private final static int MSG_FLASH_ON = 0x555;
	private final static int MSG_FLASH_OFF = 0x666;

	/**
	 * 小于等于5的时候就提醒
	 */
	private final static int ALARM_SECOND = 5;

	private TextView showTimeTv;
	private ImageView showLoadingIv;

	private long currentTime;

	private Timer timer;
	private TimerTask timerTask;

	private Timer flashTimer;
	private TimerTask flashTimerTask;
	private boolean flashFlag = false;

	private ObjectAnimator animator;

	private boolean isAnimStart = false;

	private Context context;

	private Vibrator vibrator;

	public ChronometerDesc(Context context) {
		super(context);
		init(context);
	}

	public ChronometerDesc(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ChronometerDesc(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
		if (time < 0) time = 0;
		this.currentTime = time;
		String second = CommonUtils.getNumberString(CommonUtils.getSecond(time));
		String timeStr = String.format(context.getString(R.string.second), second);
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
	 * 开始计时,每秒减1
	 */
	public void startTime() {
		final RefreshHandler handler = new RefreshHandler(this);
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				currentTime--;
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
		if (vibrator != null)
			vibrator.cancel();
		// 停止执行旋转动画
		stopRotationAnim();
		// 停止闪烁
//		setFlashOff();
	}

	private void alarm(long currentTime) {
		// 每次振动不超过500毫秒
		if (currentTime <= ALARM_SECOND) {
			vibrator.vibrate(500);
		}
		// TODO:背景闪烁
//		setFlashOn();

	}

	private void setFlashColor(int color) {
		setBackgroundColor(color);
	}

	private void setFlashFlag(boolean flashFlag) {
		this.flashFlag = flashFlag;
	}

	/**
	 * 开启闪屏
	 */
	private void setFlashOn() {
		final RefreshHandler handler = new RefreshHandler(this);
		flashTimer = new Timer();
		flashTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (!flashFlag) {
					handler.obtainMessage(MSG_FLASH_ON).sendToTarget();
				} else {
					handler.obtainMessage(MSG_FLASH_OFF).sendToTarget();
				}
			}
		};
		flashTimer.schedule(flashTimerTask, 0, 200);
	}

	/**
	 * 关闭闪屏
	 */
	private void setFlashOff() {
		if (flashTimer != null && flashTimerTask != null) {
			flashTimerTask.cancel();
			flashTimer.cancel();
		}
		flashTimerTask = null;
		flashTimer = null;
		// TODO:这里需要延迟设置，否者可能不生效
		ThreadUtils.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				setBackgroundColor(
						ResourcesUtils.getColor(R.color.none));
				flashFlag = false;
			}
		}, 200);
	}

	private static class RefreshHandler extends Handler {

		private WeakReference<ChronometerDesc> cascReference;

		RefreshHandler(ChronometerDesc cdesc) {
			cascReference = new WeakReference<>(cdesc);
		}

		@Override
		public void handleMessage(Message msg) {
			ChronometerDesc cdesc = cascReference.get();
			if (cdesc == null) return;
			switch (msg.what) {
				case MSG_REFRESH:
					cdesc.setCurrentTime((long) msg.obj);
					cdesc.alarm((long) msg.obj);
					break;
				case MSG_FLASH_ON:
					cdesc.setFlashColor(ResourcesUtils.getColor(android.R.color.black));
					cdesc.setFlashFlag(true);
					break;
				case MSG_FLASH_OFF:
					cdesc.setFlashColor(ResourcesUtils.getColor(android.R.color.white));
					cdesc.setFlashFlag(false);
					break;
			}

		}
	}


}
