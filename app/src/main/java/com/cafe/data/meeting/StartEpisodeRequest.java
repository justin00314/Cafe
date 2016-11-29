package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * 开始插话请求实体类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class StartEpisodeRequest extends MeetingInfo {

	@SerializedName("time")
	public String time;
}
