package com.cafe.data.account;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户基本信息
 * Created by Justin Z on 2016/11/9.
 * 502953057@qq.com
 */

public class UserInfo implements Serializable {
	/**
	 * 用户id
	 */
	@SerializedName("user_id")
	public String userID;
	/**
	 * 用户名
	 */
	@SerializedName("user_name")
	public String userName;
	/**
	 * 员工工号
	 */
	@SerializedName("job_number")
	public String workNumber;
	/**
	 * 用户头像地址链接
	 */
	@SerializedName("portrait")
	public String userPortrait;


}
