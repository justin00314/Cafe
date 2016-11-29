package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.data.meeting.MeetingUserInfo;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public interface MeetingDetailContract {

	interface View extends BaseView {
		/**
		 * 设置会议信息
		 * @param info
		 */
		void setMeetingInfo(MeetingUserInfo info);

	}


	interface Presenter extends BasePresenter {

		void setMeetingInfo(MeetingUserInfo info);



	}

	interface Model extends BaseModel {
		/**
		 * 获取当前的说话人
		 */
		void getNowTalker(ResponseHandlerInterface response);

		/**
		 * 加载会议过程列表
		 */
		void loadProcedureList(ResponseHandlerInterface response);

	}

}
