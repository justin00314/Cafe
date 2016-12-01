package com.cafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.cafe.common.net.HttpManager;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.UrlName;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.QueryMeetingUserResponse;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.TimeUtils;
import org.justin.utils.system.AppUtils;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * 检查状态用的Service,用于后台启动数据采集
 * Created by Justin Z on 2016/12/1.
 * 502953057@qq.com
 */

public class CheckService extends Service {

	private final static String TAG = CheckService.class.getSimpleName();

	private final static int MSG_CHECK_PRESENT_AT_MEETING = 0x0010;
	private final static long CHECK_PERIOD = 10 * 1000;

	private Timer timer;
	private TimerTask timerTask;

	private Intent funfIntent;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.i(TAG, "--CheckService 启动--");
		funfIntent = new Intent(this, FunfManagerService.class);
		startCheck();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i(TAG, "--CheckService 结束--");
		if (timerTask != null)
			timerTask.cancel();
		if (timer != null)
			timer.cancel();
		// 停止funf
		stopService(funfIntent);

	}

	private void startCheck() {
		final RefreshHandler handler = new RefreshHandler(this);
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				handler.obtainMessage(MSG_CHECK_PRESENT_AT_MEETING).sendToTarget();
			}
		};
		timer.schedule(timerTask, 500, CHECK_PERIOD);
	}


	private static class RefreshHandler extends Handler {

		private WeakReference<CheckService> reference;

		RefreshHandler(CheckService service) {
			reference = new WeakReference<>(service);
		}

		@Override
		public void handleMessage(Message msg) {
			CheckService service = reference.get();
			if (service == null) return;
			switch (msg.what) {
				case MSG_CHECK_PRESENT_AT_MEETING:
					service.checkIsPresentAtMeeting();
					break;
			}

		}
	}

	/**
	 * 检查当前用户是否在某个会议
	 */
	private void checkIsPresentAtMeeting() {
		MeetingInfo info = new MeetingInfo();
		HttpManager.postJson(this, UrlName.PRESENT_AT_MEETING.getUrl(), info,
				new JsonHttpResponseHandler<QueryMeetingUserResponse>() {
					@Override
					public void onHandleSuccess(int statusCode, Header[] headers,
					                            QueryMeetingUserResponse jsonObj) {
						handleSuccess(jsonObj);

					}

					@Override
					public void onCancel() {
						// TODO:不做任何处理

					}

					@Override
					public void onHandleFailure(String errorMsg) {
						// TODO:不做任何处理
					}

				});
	}

	/**
	 * 处理加入会议成功
	 */
	private void handleSuccess(QueryMeetingUserResponse response) {
		boolean funfFlag = AppUtils.isServiceRunning(this,
				FunfManagerService.class.getSimpleName());
		LogUtils.i(TAG, "funf启动状态-->" + funfFlag);
		// 启动funf逻辑：当前用户参加了某个会议，并且会议已经开始,funf没有启动
		if (response.data != null && response.data.id != -1) {
			long startTime = TimeUtils.dateToTimeStamp(response.data.startTime,
					TimeUtils.Template.YMDHMS) / 1000;
			long currentTime = new Date().getTime() / 1000;
			LogUtils.i(TAG, "会议开始时间-->" + startTime);
			LogUtils.i(TAG, "当前时间-->" + currentTime);
			if (startTime != -1 && currentTime >= startTime && !funfFlag) {
				LogUtils.i(TAG, "启动funf-->");
				startService(funfIntent);
			}
		}
		// 停止funf逻辑:用户没有参与会议,并且funf已经启动
		else if (funfFlag) {
			LogUtils.i(TAG, "停止funf-->");
			stopService(funfIntent);
		}
	}


}
