package com.cafe.data.meeting;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Z on 2016/11/23.
 * 502953057@qq.com
 */

public class StatusInfo {

	@SerializedName("status")
	public int status;

	public StatusInfo(){}

	public StatusInfo(int status){
		this.status = status;
	}
}
