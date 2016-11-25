package com.cafe.data.meeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 会议实体类
 * Created by Justin Z on 2016/11/9.
 * 502953057@qq.com
 */

public class MeetingInfo implements Serializable {

	public static final int TYPE_THEME = 1;
	public static final int TYPE_BRAIN_STORM = 2;

	/**
	 * 会议ID
	 */
	@SerializedName("meeting_id")
	public int id;
	/**
	 * 会议名字
	 */
	@SerializedName("meeting_name")
	public String name;
	/**
	 * 会议详情
	 */
	@SerializedName("meeting_desc")
	public String desc;
	/**
	 * 会议开始时间
	 */
	@SerializedName("start_time")
	public String startTime;
	/**
	 * 会议结束时间
	 */
	@SerializedName("end_time")
	public String endTime;
	/**
	 * 会议创建时间
	 */
	@SerializedName("create_time")
	public String createTime;
	/**
	 * 会议室id
	 */
	@SerializedName("meeting_room_id")
	public String meetingRoomId;
	/**
	 * 会议室名字
	 */
	@SerializedName("meeting_room_name")
	public String meetingRoomName;
	/**
	 * 参会人数
	 */
	@SerializedName("attendance")
	public int attendance;
	/**
	 * 会议类型,头脑风暴或主题会议
	 */
	@SerializedName("meeting_type")
	public int type;
	/**
	 * 会议状态
	 */
	@Expose
	@SerializedName("meeting_status")
	public int state;


}
