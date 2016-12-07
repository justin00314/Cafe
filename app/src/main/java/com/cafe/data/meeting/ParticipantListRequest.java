package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * 查询会议参与人列表请求实体类
 * Created by Justin Z on 2016/12/7.
 * 502953057@qq.com
 */

public class ParticipantListRequest extends MeetingInfo {

	@SerializedName("page_number")
	public int pageNumber;
	
	@SerializedName("page_size")
	public int pageSize;
}
