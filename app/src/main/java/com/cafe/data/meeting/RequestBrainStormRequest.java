package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 请求创建头脑风暴请求实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class RequestBrainStormRequest implements Serializable {

	@SerializedName("time")
	public String time;
}
