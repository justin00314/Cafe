package com.cafe.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

import com.cafe.R;
import com.cafe.common.DiskCacheMaxSize;
import com.cafe.common.DiskCachePath;
import com.cafe.contract.TestContract;
import com.cafe.funf.ActivityAction;
import com.cafe.funf.AudioAction;
import com.cafe.funf.base.ActionManager;

import org.justin.utils.common.LogUtils;

/**
 * 数据采集管理服务
 * Created by Justin Z on 2016/11/1.
 * 502953057@qq.com
 */

public class FunfManagerService extends Service {

	private final static String TAG = FunfManagerService.class.getSimpleName();
	/**
	 * 通知栏ID
	 */
	private final static int NOTIFICATION_ID = 0x00023;

	/**
	 * 电源锁
	 */
	private PowerManager.WakeLock mWakeLock;
	/**
	 * 电话管理器对象
	 */
	private TelephonyManager mTelephonyManager;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.i(TAG, "--CollectDataService启动--");
		// 获取电源锁,保证长时间运行不被回收
		acquireWakeLock();
		// 初始化电话监听
		registerTelListener();
		// 启动成为前台服务，避免被系统回收
		startForegroundCompat();
		// 启动所有的Action
		startActions();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i(TAG, "--CollectDataService结束--");
		releaseWakeLock();
		// 结束前台服务
		stopForeground(true);
		// 移除所有的Action
		ActionManager.getInstance().removeActions();
		// 移除电话监听
		unregisterTelListener();
	}

	/**
	 * 获取电源锁
	 */
	private void acquireWakeLock() {
		LogUtils.i(TAG, "--获取电源锁--");
		if (mWakeLock == null) {
			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
			// 用户按了电源键之后任然保持CPU运转
			mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					this.getClass().getCanonicalName());
			if (mWakeLock != null)
				mWakeLock.acquire();
		}
	}

	/**
	 * 释放电源锁
	 */
	private void releaseWakeLock() {
		LogUtils.i(TAG, "--释放电源锁--");
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	/**
	 * 初始化电话监听器
	 */
	private void registerTelListener() {
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 注册电话状态监听，在Service结束的时候注销掉
		mTelephonyManager.listen(telPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private void unregisterTelListener() {
		mTelephonyManager.listen(telPhoneListener, PhoneStateListener.LISTEN_NONE);
	}

	private PhoneStateListener telPhoneListener = new PhoneStateListener() {
		// OFFHOOK状态在电话拨号的时候会回调多次
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
				// 电话响起
				case TelephonyManager.CALL_STATE_RINGING:
					LogUtils.i(TAG, "--电话响铃--");
					ActionManager.getInstance().stopActions();
					break;
				// 接起电话
				case TelephonyManager.CALL_STATE_OFFHOOK:
					// System.out.println("对方电话-->" + incomingNumber);
					LogUtils.i(TAG, "--电话接起--");
					ActionManager.getInstance().stopActions();
					break;
				// 无任何状态
				case TelephonyManager.CALL_STATE_IDLE:
					// 停止录音并通知界面
					LogUtils.i(TAG, "--电话挂断--");
					startActions();
					break;
			}

		}
	};

	/**
	 * 启动所有的Action
	 */
	private void startActions() {
		if (!ActionManager.getInstance().hasAction(AudioAction.class.getSimpleName())) {
			AudioAction audioAction = new AudioAction(this,
					DiskCachePath.AUDIO_DB, DiskCacheMaxSize.AUDIO_DB);
			ActionManager.getInstance().addAction(audioAction);
		}
		if (!ActionManager.getInstance().hasAction(ActivityAction.class.getSimpleName())) {
			ActivityAction activityAction = new ActivityAction(this,
					DiskCachePath.ACTIVITY_LEVEL, DiskCacheMaxSize.ACTIVITY_LEVEl);
			ActionManager.getInstance().addAction(activityAction);
		}
		ActionManager.getInstance().startActions();
	}

	/**
	 * 设置为前台服务
	 */
	private void startForegroundCompat() {
		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
		Intent notificationIntent = new Intent(this, TestContract.class);
		PendingIntent pdIntent = PendingIntent.getActivity(this, 0, notificationIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setTicker(getString(R.string.audio_capture));
		builder.setContent(contentView);
		builder.setContentIntent(pdIntent);
		// 设置服务为前台服务
		startForeground(NOTIFICATION_ID, builder.build());
	}


}
