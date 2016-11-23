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


}
