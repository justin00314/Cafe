package com.ai2020lab.cafe.common;

import android.text.TextUtils;

import com.ai2020lab.aiutils.common.LogUtils;
import com.ai2020lab.aiutils.common.StringUtils;
import com.ai2020lab.aiutils.common.TimeUtils;

import java.util.Date;

/**
 *
 * 通用工具类
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */

public class CommonUtils {
	private final static String TAG = CommonUtils.class.getSimpleName();

	/**
	 * 获取当前的显示时间，格式为 yyyy-MM-dd
	 *
	 * @return 返回当前显示时间
	 */
	public static String getCurrentDate() {
		// 获取当前时间戳
		long currentTime = new Date().getTime();
		String currentTimeStr = TimeUtils.formatTimeStamp(currentTime, TimeUtils.Template.YMD);
		LogUtils.i(TAG, "当前时间为:" + currentTimeStr);
		return currentTimeStr;
	}


	/**
	 * 获取当前年份
	 *
	 * @return 返回当前年份
	 */
	public static int getCurrentYear() {
		String currentTimeStr = getCurrentDate();
		if (TextUtils.isEmpty(currentTimeStr)) {
			LogUtils.i(TAG, "当前时间为空");
			return -1;
		}
		String year = currentTimeStr.substring(0, 4);
//		LogUtils.i(TAG, "当前年份：" + year);
		return StringUtils.parseInt(year);
	}

	/**
	 * 获取当前月份
	 *
	 * @return 返回当前月份
	 */
	public static int getCurrentMonth() {
		String currentTimeStr = getCurrentDate();
		if (TextUtils.isEmpty(currentTimeStr)) {
			LogUtils.i(TAG, "当前时间为空");
			return -1;
		}
		String month = currentTimeStr.substring(5, 7);
//		LogUtils.i(TAG, "当前月份：" + month);
		return StringUtils.parseInt(month);
	}

	/**
	 * 获取当前日子
	 *
	 * @return 返回当前日子
	 */
	public static int getCurrentDay() {
		String currentTimeStr = getCurrentDate();
		if (TextUtils.isEmpty(currentTimeStr)) {
			LogUtils.i(TAG, "当前时间为空");
			return -1;
		}
		String day = currentTimeStr.substring(8, 10);
//		LogUtils.i(TAG, "当前日期：" + day);
		return StringUtils.parseInt(day);
	}


}
