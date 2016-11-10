package com.ai2020lab.cafe.data.meeting;

import com.ai2020lab.cafe.data.account.UserInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 会议实体类
 * Created by Justin Z on 2016/11/9.
 * 502953057@qq.com
 */

public class MeetingInfo implements Serializable {

	/**
	 * 会议ID
	 */
	@Expose
	@SerializedName("id")
	public String id;
	/**
	 * 会议名字
	 */
	@Expose
	@SerializedName("name")
	public String name;
	/**
	 * 会议开始时间
	 */
	@Expose
	@SerializedName("start_time")
	public String startTime;
	/**
	 * 开会地点
	 */
	@Expose
	@SerializedName("address")
	public String address;
	/**
	 * 参会人数
	 */
	@Expose
	@SerializedName("member_number")
	public int memberNumber;
	/**
	 * 会议类型,头脑风暴或主题会议
	 */
	@Expose
	@SerializedName("type")
	public int type;
	/**
	 * 会议状态
	 */
	@Expose
	@SerializedName("state")
	public int state;
	/**
	 * 是否自己创建标志位
	 */
	@Expose
	@SerializedName("created_flag")
	public boolean createdFlag;
	/**
	 * 是否参与标志位
	 */
	@Expose
	@SerializedName("participated_flag")
	public boolean participatedFlag;
	/**
	 * 参与者列表
	 */
	@Expose
	@SerializedName("participated_user_list")
	public List<UserInfo> participatedUsers;

	/**
	 * 创建者对象
	 */
	@Expose
	@SerializedName("created_user")
	public UserInfo createdUser;

}
