package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * 查询会议说话记录请求实体类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class ProcedureListRequest extends MeetingInfo {


	@SerializedName("filter_time")
	public String filterTime;
}
