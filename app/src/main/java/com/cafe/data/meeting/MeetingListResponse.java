package com.cafe.data.meeting;

import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 查询会议列表响应实体类
 * Created by Justin Z on 2016/11/10.
 * 502953057@qq.com
 */

public class MeetingListResponse extends ResponseData<MeetingListResponse.MeetingListResult> {


	public class MeetingListResult {

		/**
		 * 过滤时间
		 */
		@SerializedName("filter_time")
		public String filterTime;
		/**
		 * 用户会议列表
		 */
		@SerializedName("meeting_list")
		public List<MeetingUserInfo> meetingInfos;
	}
}
