package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * 开始主题请求实体类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class StartThemeRequest extends MeetingInfo{

	@SerializedName("time")
	public String time;
}
