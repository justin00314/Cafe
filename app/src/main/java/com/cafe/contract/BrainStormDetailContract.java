package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.data.meeting.MeetingUserInfo;
import com.cafe.data.meeting.ProcedureInfo;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.List;

/**
 * 临时会议详情接口协议类
 * Created by Justin Z on 2016/12/6.
 * 502953057@qq.com
 */

public interface BrainStormDetailContract {
	interface View extends BaseView {
		/**
		 * 开始监听手机摇一摇
		 */
		void startShake();

		/**
		 * 停止监听手机摇一摇
		 */
		void stopShake();


		/**
		 * 判断是否已经开始手机摇一摇监听
		 */
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
		 * 加载会议说话列表
		 */
		void loadProcedureList(List<ProcedureInfo> procedureInfos);

	}


	interface Presenter extends BasePresenter {

		/**
		 * 加载会议参与人列表
		 */
		void loadMeetingParticipantList(MeetingUserInfo info);

		/**
		 * 收到用户在会广播后处理,会议已经解散就退出会议
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
		 * 处理摇一摇手机
		 */
		void shakePhoneForBrainStorm();

		/**
		 * 请求解散会议
		 */
		void dismissBrainStorm(MeetingUserInfo info);



	}

	interface Model extends BaseModel {

		/**
		 * 查询会议参与人列表
		 */
		void queryMeetingParticipantList(MeetingUserInfo info, ResponseHandlerInterface response);

		/**
		 * 请求解散会议
		 */
		void requestForDismissMeeting(MeetingUserInfo info, ResponseHandlerInterface response);

		/**
		 * 查询当前用户是否在某个会议
		 */
		void getIsAtSomeMeeting(ResponseHandlerInterface response);
	}
}
