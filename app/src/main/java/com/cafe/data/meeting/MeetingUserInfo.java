package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * 用户会议对象
 * Created by Justin Z on 2016/11/23.
 * 502953057@qq.com
 */

public class MeetingUserInfo extends MeetingInfo {

	/**
	 * 是否自己创建
	 */
	@SerializedName("created_flag")
	public boolean createdFlag;
	/**
	 * 是否参与
	 */
	@SerializedName("participated_flag")
	public boolean participatedFlag;


}
