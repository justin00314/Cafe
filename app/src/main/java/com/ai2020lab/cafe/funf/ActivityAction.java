package com.ai2020lab.cafe.funf;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.ai2020lab.aiutils.common.JsonUtils;
import com.ai2020lab.aiutils.common.LogUtils;
import com.ai2020lab.aiutils.common.TimeUtils;
import com.ai2020lab.cafe.common.TimeUtil;
import com.ai2020lab.cafe.data.funf.FunfData;
import com.ai2020lab.cafe.data.funf.ProbeName;
import com.ai2020lab.cafe.funf.base.WriteAction;
import com.google.gson.JsonObject;

import java.util.Date;

/**
 * 活动量等级采集Action
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */

public class ActivityAction extends WriteAction {

	private final static String TAG = ActivityAction.class.getSimpleName();

	public final static int ACTIVITY_LEVEL_NONE = 0;
	public final static int ACTIVITY_LEVEL_LOW = 1;
	public final static int ACTIVITY_LEVEL_HIGH = 2;

	private SensorManager sensorManager;
	private Sensor sensor;
	private SensorEventListener sensorListener;

	private double intervalStartTime;
	private float varianceSum;
	private float avg;
	private float sum;
	private int count;

	/**
	 * 构造方法
	 *
	 * @param context  Context
	 * @param savePath String
	 * @param maxSize  long
	 */
	public ActivityAction(Context context, String savePath, long maxSize) {
		super(context, savePath, maxSize);
		init();
	}

	private void init() {
		sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent sensorEvent) {
				calculateActivity(sensorEvent);
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};

	}

	private void reset() {
		// If more than an interval away, start a new scan
		varianceSum = avg = sum = count = 0;
	}

	private void update(float x, float y, float z) {
		// Iteratively calculate variance sum
//		LogUtils.i(TAG, "x-->" + x);
//		LogUtils.i(TAG, "y-->" + y);
//		LogUtils.i(TAG, "z-->" + z);
		count++;
		float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
		float newAvg = (count - 1) * avg / count + magnitude / count;
		float deltaAvg = newAvg - avg;
		varianceSum += (magnitude - newAvg) * (magnitude - newAvg)
				- 2 * (sum - (count - 1) * avg)
				+ (count - 1) * (deltaAvg * deltaAvg);
		sum += magnitude;
		avg = newAvg;
	}

	private void intervalReset(long timestamp) {
		// Calculate activity and reset
		FunfData funfData = new FunfData();
		funfData.probeName = ProbeName.ACTIVITY;
		funfData.timestamp = timestamp;
		funfData.time = TimeUtils.formatTimeStamp(timestamp,
				TimeUtils.Template.YMDHMS);
		if (varianceSum >= 10.0f) {
			funfData.value = ACTIVITY_LEVEL_HIGH + "";
			LogUtils.i(TAG, "level-->high");
		} else if (varianceSum < 10.0f && varianceSum > 3.0f) {
			funfData.value = ACTIVITY_LEVEL_LOW + "";
			LogUtils.i(TAG, "level-->low");
		} else {
			funfData.value = ACTIVITY_LEVEL_NONE + "";
			LogUtils.i(TAG, "level-->none");
		}
		String json = JsonUtils.getInstance().serializeToJson(funfData);
		// 保存数据
		saveData(funfData.time, json);
		varianceSum = avg = sum = count = 0;
	}

	/**
	 * 计算活动等级
	 */
	private void calculateActivity(SensorEvent event) {

		double timestamp = TimeUtil.uptimeNanosToTimestamp(event.timestamp).doubleValue();

//		long timestamp = new Date().getTime();
//		LogUtils.i(TAG, "timestamp-->" + timestamp);
		double interval = 1;
		if (intervalStartTime == 0.0 || (timestamp >= intervalStartTime + 2 * interval)) {
			reset();
			intervalStartTime = timestamp;
		} else if (timestamp >= intervalStartTime + interval) {
			intervalReset(new Date().getTime());
			intervalStartTime = timestamp;
		}

		update(event.values[0], event.values[1], event.values[2]);

	}

	/**
	 * 注册监听
	 */
	private void registerSensorListener() {
		if (sensorManager != null) {
			sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	/**
	 * 移除监听
	 */
	private void unRegisterSensorListener() {
		// 界面回到后台解除传感器监听
		if (sensorManager != null)
			sensorManager.unregisterListener(sensorListener);
	}


	@Override
	public void onStart() {
		super.onStart();
		LogUtils.i(TAG, "--采集活动等级Action开始--");
		registerSensorListener();
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtils.i(TAG, "--采集活动等级Action结束--");
		unRegisterSensorListener();

	}
}
