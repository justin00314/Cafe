package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.data.account.UserInfo;
import com.cafe.data.meeting.GetNowTalkerResponse;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.ProcedureInfo;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.List;

/**
 * 主题会议详情接口协议类
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public interface ThemeDetailContract {

	interface View extends BaseView {

		void startShake();

		void stopShake();

		boolean isTap();

		void setIsTap(boolean isTap);

		boolean isStartShake();

		/**
		 * 倒计时是否开始
		 */
		boolean isTimeDescStart();

		/**
		 * 退出界面
		 */
		void finishActivity();

		/**
		 * 开始会议计时
		 */
		void startMeetingTime(long time);

		/**
		 * 结束会议计时
		 */
		void stopMeetingTime();

		/**
		 * 开始插话后刷新界面
		 */
		void refreshAfterStartEpisode();

		/**
		 * 结束插话后刷新界面
		 */
		void refreshAfterStopEpisode();

		/**
		 * 设置当前说话人
		 */
		void setNowTalker(GetNowTalkerResponse.GetNowTalkerResult result);

		/**
		 * 加载会议说话列表
		 */
		void loadProcedureList(List<ProcedureInfo> procedureInfos);

	}


	interface Presenter extends BasePresenter {

		/**
		 * 收到用户在会广播后处理
		 */
		void doReceiveAtMeeting(MeetingUserInfo info);

		/**
		 * 开始会议计时
		 */
		void startMeetingTime(MeetingUserInfo info);

		/**
		 * 结束会议计时
		 */
		void stopMeetingTime();
		/**
		 * 获取当前说话人
		 */
		void getNowTalker(MeetingUserInfo info);

		/**
		 * 获取会议过程列表
		 */
		void loadProcedureList(MeetingUserInfo info);

		/**
		 * 操作开始主题或结束主题
		 */
		void operateTheme(MeetingUserInfo info);

		/**
		 * 操作开始插话或结束插话
		 */
		void operateEpisode(MeetingUserInfo info);

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
		 * 查询当前登录人的未结束事件
		 */
		void queryUnstopEvent(MeetingUserInfo info, ResponseHandlerInterface response);
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
