package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.data.meeting.MeetingUserInfo;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 主题会议详情接口协议类
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public interface ThemeDetailContract {

	interface View extends BaseView {

		/**
		 *  展示插话过程中的倒计时，这个时候正计时隐藏
		 */
		void showEpisode(boolean flag);

	}


	interface Presenter extends BasePresenter {
		/**
		 * 获取当前说话人
		 */
		void getNowTalker(MeetingUserInfo info);
		/**
		 * 获取会议过程列表
		 */
		void loadProcedureList(MeetingUserInfo info);

		/**
		 * 开始主题
		 */
		void startTheme(MeetingUserInfo info);

		/**
		 * 结束主题
		 */
		void stopTheme(MeetingUserInfo info);

		/**
		 * 开始插话
		 */
		void startEpisode(MeetingUserInfo info);

		/**
		 * 结束插话
		 */
		void stopEpisode(MeetingUserInfo info);

	}

	interface Model extends BaseModel {
		/**
		 * 获取当前的说话人
		 */
		void getNowTalker(MeetingUserInfo info, ResponseHandlerInterface response);

		/**
		 * 加载会议过程列表
		 */
		void loadProcedureList(MeetingUserInfo info, ResponseHandlerInterface response);

		/**
		 * 开始主题
		 */
		void startTheme(MeetingUserInfo info, ResponseHandlerInterface response);

		/**
		 * 结束主题
		 */
		void stopTheme(MeetingUserInfo info, ResponseHandlerInterface response);

		/**
		 * 开始插话
		 */
		void startEpisode(MeetingUserInfo info, ResponseHandlerInterface response);

		/**
		 * 结束插话
		 */
		void stopEpisode(MeetingUserInfo info, ResponseHandlerInterface response);

	}

}
