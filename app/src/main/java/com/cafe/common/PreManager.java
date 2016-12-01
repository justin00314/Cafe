package com.cafe.common;

import android.content.Context;

import org.justin.utils.storage.PreferencesUtils;

/**
 * SharedPreferences工具类
 * Created by Justin Z on 2016/11/23.
 * 502953057@qq.com
 */

public class PreManager {
	private final static String TOKEN = "token";
	private final static String MEETING_LIST_FILTER_TIME = "meeting_list_filter_time";
	private final static String MEETING_PROCEDURE_FILTER_TIME = "meeting_procedure_filter_time";

	private final static String START_TOPIC_FLAG = "start_topic_flag";
	private final static String START_EPISODE_FLAG = "start_episode_flag";
	/**
	 * 保存token
	 * @param context Context
	 * @param token String
	 */
	public static void setToken(Context context, String token) {
		PreferencesUtils.setString(context, TOKEN, token);
	}

	/**
	 * 获取token
	 * @param context Context
	 * @return String
	 */
	public static String getToken(Context context) {
		return PreferencesUtils.getString(context, TOKEN, "");
	}


	public static void setMeetingListFilterTime(Context context, String filterTime){
		PreferencesUtils.setString(context, MEETING_LIST_FILTER_TIME, filterTime);
	}

	public static String getMeetingListFilterTime(Context context){
		return PreferencesUtils.getString(context, MEETING_LIST_FILTER_TIME, "");
	}

	public static void setProcedureFilterTime(Context context, String filterTime){
		PreferencesUtils.setString(context, MEETING_PROCEDURE_FILTER_TIME, filterTime);
	}

	public static String getProcedureFilterTime(Context context){
		return PreferencesUtils.getString(context, MEETING_PROCEDURE_FILTER_TIME, "");
	}

	public static void setStartTopicFlag(Context context, boolean flag){
		PreferencesUtils.setBoolean(context, START_TOPIC_FLAG, flag);
	}

	public static boolean getStartTopicFlag(Context context){
		return PreferencesUtils.getBoolean(context, START_TOPIC_FLAG, false);
	}

	public static void setStartEpisodeFlag(Context context, boolean flag){
		PreferencesUtils.setBoolean(context, START_EPISODE_FLAG, flag);
	}

	public static boolean getStartEpisodeFlag(Context context){
		return PreferencesUtils.getBoolean(context, START_EPISODE_FLAG, false);
	}


}
