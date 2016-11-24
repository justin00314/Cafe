package com.cafe.data.meeting;

import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

/**
 * 创建会议响应实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class CreateMeetingResponse extends ResponseData<CreateMeetingResponse.CreateMeetingResult> {

	public class CreateMeetingResult {

		@SerializedName("result")
		public boolean result;

		@SerializedName("meeting_id")
		public int id;

	}
}
