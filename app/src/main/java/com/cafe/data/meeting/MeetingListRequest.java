package com.cafe.data.meeting;

import com.cafe.data.account.UserInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 查询会议列表请求实体类
 * Created by Justin Z on 2016/11/10.
 * 502953057@qq.com
 */

public class MeetingListRequest extends UserInfo {

	@SerializedName("filter_time")
	public String filterTime;

	@SerializedName("meeting_status_list")
	public List<StatusInfo> statusInfoList;

}
