package com.cafe.data.meeting;

import com.cafe.data.account.UserInfo;
import com.cafe.data.base.ResponseData;

/**
 * 获取当前说话人响应实体类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class GetNowTalkerResponse extends ResponseData<GetNowTalkerResponse.GetNowTalkerResult> {

	public class GetNowTalkerResult extends UserInfo {


	}
}