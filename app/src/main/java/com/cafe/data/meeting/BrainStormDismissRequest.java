package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * 请求解散头脑风暴请求实体类
 * Created by Justin Z on 2016/12/7.
 * 502953057@qq.com
 */

public class BrainStormDismissRequest extends MeetingUserInfo {

	@SerializedName("time")
	public String time;
}
