package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 创建会议请求实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class CreateMeetingRequest implements Serializable {

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
     * 会议室id
     */
    @SerializedName("meeting_room_id")
    public int meetingRoomId;
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
}
