package com.cafe.data.account;

import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

/**
 * 用户登出响应实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class LogoutResponse extends ResponseData<LogoutResponse.LogoutResult> {


	public class LogoutResult {

		@SerializedName("result")
		public boolean result;
	}


}
