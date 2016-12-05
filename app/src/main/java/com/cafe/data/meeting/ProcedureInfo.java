package com.cafe.data.meeting;

import com.cafe.data.account.UserInfo;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class ProcedureInfo extends UserInfo {

	@SerializedName("duration")
	public int duration;

	@SerializedName("speakType")
	public int type;

	@SerializedName("state")
	public int state;

	@SerializedName("stop_time")
	public String stopTime;

	@SerializedName("stop_timestamp")
	public long stopTimestamp;
}
