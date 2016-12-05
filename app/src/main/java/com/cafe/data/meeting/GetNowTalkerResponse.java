package com.cafe.data.meeting;

import com.cafe.data.account.UserInfo;
import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

/**
 * 获取当前说话人响应实体类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class GetNowTalkerResponse extends ResponseData<GetNowTalkerResponse.GetNowTalkerResult> {

	public class GetNowTalkerResult extends UserInfo {

		@SerializedName("speak_type")
		public int speakType;

		@SerializedName("is_self")
		public boolean isSelfFlag;

	}
}
