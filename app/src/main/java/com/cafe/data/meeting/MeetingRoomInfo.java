package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class MeetingRoomInfo implements Serializable {

	@SerializedName("meeting_room_id")
	public int id;

	@SerializedName("meeting_room_name")
	public String name;
}
