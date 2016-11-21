package com.cafe.data.funf;

import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Justin Z on 2016/11/3.
 * 502953057@qq.com
 */

public class FunfDataResponse extends ResponseData<FunfDataResponse.FunfDataResult> {

	public class FunfDataResult {

		@SerializedName("success_upload_list")
		public List<FunfData> funfDatas;
	}
}
