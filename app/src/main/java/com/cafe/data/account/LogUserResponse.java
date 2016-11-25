package com.cafe.data.account;

import com.cafe.data.base.ResponseData;

/**
 * 获取当前登录人响应实体类
 * Created by Justin Z on 2016/11/24.
 * 502953057@qq.com
 */

public class LogUserResponse extends ResponseData<LogUserResponse.Result>{


	public class Result extends UserInfo {

	}
}
