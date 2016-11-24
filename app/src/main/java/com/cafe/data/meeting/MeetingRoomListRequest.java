package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 查询可用会议室列表请求实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class MeetingRoomListRequest implements Serializable {

	@SerializedName("start_time")
	public String startTime;

	@SerializedName("end_time")
	public String endTime;
}
