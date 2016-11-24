package com.cafe.data.account;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户登出请求实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class LogoutRequest implements Serializable {

	@SerializedName("device_type")
	public String deviceType;
}
