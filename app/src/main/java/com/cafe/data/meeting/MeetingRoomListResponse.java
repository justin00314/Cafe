package com.cafe.data.meeting;

import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 查询可用会议室响应实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class MeetingRoomListResponse extends ResponseData<MeetingRoomListResponse.MeetingRoomListResult> {

	public class MeetingRoomListResult {

		@SerializedName("meeting_room_list")
		public List<MeetingRoomInfo> meetingRoomInfos;
	}
}
