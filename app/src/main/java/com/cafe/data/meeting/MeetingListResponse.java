package com.cafe.data.meeting;

import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 查询会议列表响应实体类
 * Created by Justin Z on 2016/11/10.
 * 502953057@qq.com
 */

public class MeetingListResponse extends ResponseData<MeetingListResponse.MeetingListResult> {


	public class MeetingListResult{

		@Expose
		@SerializedName("meeting_list")
		public List<MeetingInfo> meetingInfos;
	}
}
