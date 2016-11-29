package com.cafe.common;

import android.text.TextUtils;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.StringUtils;
import org.justin.utils.common.TimeUtils;

import java.util.Date;

/**
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

	/**
	 * 获取秒的小时位，如果大于99则返回99
	 *
	 * @param time 秒
	 * @return 返回秒的小时位
	 */
	public static int getHour(long time) {
		int hour = (int) (time / 3600);
		if (hour > 99) hour = 99;
		return hour;
	}

	/**
	 * 获取秒的分钟位
	 *
	 * @param time 秒
	 * @return 返回秒的分钟位
	 */
	public static int getMinute(long time) {
		int hourRemainder = (int) (time % 3600);
		return hourRemainder / 60;
	}

	public static String getNumberString(int num){
		String str = num + "";
		switch(num){
			case 0:
				str = "00";
				break;
			case 1:
				str = "01";
				break;
			case 2:
				str = "02";
				break;
			case 3:
				str = "03";
				break;
			case 4:
				str = "04";
				break;
			case 5:
				str = "05";
				break;
			case 6:
				str = "06";
				break;
			case 7:
				str = "07";
				break;
			case 8:
				str = "08";
				break;
			case 9:
				str = "09";
				break;
		}
		return str;
	}

	/**
	 * 获取秒的秒位
	 *
	 * @param time 秒
	 * @return 返回秒的秒位
	 */
	public static int getSecond(long time) {
		return (int) (time % 60);
	}

	/**
	 * 4舍5入只保留整数
	 */
	public static int roundInt(float num) {
		return (int) (num + 0.5);
	}
}
