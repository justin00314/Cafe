package com.cafe.data.meeting;

import com.cafe.data.account.UserInfo;
import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 查询会议参与人响应实体类
 * Created by Justin Z on 2016/12/7.
 * 502953057@qq.com
 */

public class MeetingParticipantListResponse extends ResponseData<MeetingParticipantListResponse
		.ParticipantListResult> {

	public class ParticipantListResult {

		@SerializedName("user_list")
		public List<UserInfo> participantList;
	}
}
