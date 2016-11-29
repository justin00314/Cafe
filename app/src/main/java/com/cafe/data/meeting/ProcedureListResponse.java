package com.cafe.data.meeting;

import com.cafe.data.base.ResponseData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 查询会议说话记录响应实体类
 * Created by Justin Z on 2016/11/29.
 * 502953057@qq.com
 */

public class ProcedureListResponse extends ResponseData<ProcedureListResponse.ProcedureListResult> {

	public class ProcedureListResult {

		@SerializedName("filter_time")
		public String filterTime;

		@SerializedName("procedure_list")
		public List<ProcedureInfo> procedureInfos;

	}
}
