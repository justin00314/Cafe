package com.cafe.common;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

import org.justin.utils.common.LogUtils;

/**
 * 摇一摇手机工具类
 * Created by Justin Z on 2016/12/1.
 * 502953057@qq.com
 */

public class ShakePhoneUtils {

	private final static String TAG = ShakePhoneUtils.class.getSimpleName();

	// 控制摇晃灵敏度
	private static final int SHAKE_SPEED = 5500;

	private static final int UPDATE_INTERVAL_TIME = 50;

	private long lastUpdateTime;

	private SensorManager sensorManager;

	private Vibrator vibrator;

	private OnShakeListener onShakeListener;

	private float lastX;
	private float lastY;
	private float lastZ;


	private ShakePhoneUtils() {
	}

	private static class SingletonHolder {
		public final static ShakePhoneUtils SINGLETON = new ShakePhoneUtils();
	}

	public static ShakePhoneUtils getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private void init(Context context) {
		if (sensorManager == null)
			sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (vibrator == null)
			vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/**
	 * 开始摇晃
	 */
	public void startShake(Context context, OnShakeListener onShakeListener) {
		init(context);
		this.onShakeListener = onShakeListener;
		sensorManager.registerListener(sensorEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * 停止摇晃
	 */
	public void stopShake() {
		if (sensorManager != null)
			sensorManager.unregisterListener(sensorEventListener);
	}

	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			long currentUpdateTime = System.currentTimeMillis();
			long timeInterval = currentUpdateTime - lastUpdateTime;
			if (timeInterval < UPDATE_INTERVAL_TIME) {
				LogUtils.i(TAG, "摇晃不能太频繁-->");
				return;
			}
			lastUpdateTime = currentUpdateTime;
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0]; // x轴方向的重力加速度，向右为正
			float y = values[1]; // y轴方向的重力加速度，向前为正
			float z = values[2]; // z轴方向的重力加速度，向上为正

			float deltaX = x - lastX;
			float deltaY = y - lastY;
			float deltaZ = z - lastZ;
			lastX = x;
			lastY = y;
			lastZ = z;
			double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
					/ timeInterval * 10000;
			if (speed > SHAKE_SPEED && onShakeListener != null) {
				onShakeListener.onShake();
				if (vibrator != null) {
					vibrator.vibrate(300);
				}
			}

//			int medumValue = 30;
//			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
//					|| Math.abs(z) > medumValue) {
//				long endTime = System.currentTimeMillis();
//				long startTime = 0;
//				if (endTime - startTime < 2000) {
//					return;
//				}
//				// 手机振动提示
//				if (vibrator != null) {
//					// 伴有震动
//					vibrator.vibrate(500);
//				}
//				if (onShakeListener != null)
//					onShakeListener.onShake();
//
//
//			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	public interface OnShakeListener {
		void onShake();
	}


}
