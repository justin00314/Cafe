package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * 停止主题请求实体类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class StopThemeRequest extends MeetingInfo {

	@SerializedName("time")
	public String time;
}
