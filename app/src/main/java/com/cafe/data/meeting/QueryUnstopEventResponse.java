package com.cafe.data.meeting;

import com.cafe.data.base.ResponseData;
import com.cafe.data.base.Result;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Z on 2016/12/1.
 * 502953057@qq.com
 */

public class QueryUnstopEventResponse extends ResponseData<QueryUnstopEventResponse.QueryUnstopResult> {

	public class QueryUnstopResult extends Result {

		@SerializedName("speak_type")
		public int speakType;


	}

}
