package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.data.account.UserInfo;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingUserInfo;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.List;

/**
 * Created by Justin Z on 2016/11/7.
 * 502953057@qq.com
 */

public interface MeetingListContract {

	/**
	 * View的接口方法，由Activity去实现
	 */
	interface View extends BaseView {

		/**
		 * 加入成功之后刷新
		 */
		void refreshAfterJoin(MeetingUserInfo info);

		/**
		 * 退出成功之后刷新
		 */
		void refreshAfterQuit(MeetingUserInfo info);

		/**
		 * 取消成功之后刷新
		 */
		void refreshAfterCancel(MeetingUserInfo info);

		/**
		 * 解散成功之后刷新
		 */
		void refreshAfterDismiss(MeetingUserInfo info);

		void loadMeetingList(List<MeetingUserInfo> meetingInfoList);

		/**
		 * 设置用户信息显示
		 */
		void setUserInfo(UserInfo userInfo);

		/**
		 * 提示创建头脑风暴会议
		 */
		void promptShakePhone();

		/**
		 * 显示二维码对话框
		 */
		void showQRcodeDialog(MeetingUserInfo meetingInfo);

		/**
		 * 跳转到主题会议详情界面
		 */
		void skipToThemeDetailActivity(MeetingUserInfo info);

		/**
		 * 跳转到头脑风暴详情界面
		 */
		void skipToBrainStormActivity(MeetingUserInfo info);

		/**
		 * 跳转到登录界面
		 */
		void skipToLoginActivity();

		/**
		 * 跳转到搜索界面
		 */
		void skipToSearchActivity();

		/**
		 * 跳转到二维码扫描界面
		 */
		void skipToScanQRCodeActivity();

		/**
		 * 跳转到创建主题会议界面
		 */
		void skipToCreateMeetingActivity();

	}

	/**
	 * Presenter接口方法，一般是Activity中的业务逻辑方法
	 */
	interface Presenter extends BasePresenter {


		void loadMeetingListTest();

		/**
		 * 加载会议列表
		 */
		void loadMeetingList();

		/**
		 * 获取当前登录用户信息
		 */
		void getUserInfo();

		/**
		 * 登出
		 */
		void logout();

		/**
		 * 搜索
		 */
		void search();

		/**
		 * 扫描二维码
		 */
		void scanQRCode();

		/**
		 * 创建主题会议
		 */
		void createTheme();

		/**
		 * 处理创建主题会议返回
		 */
		void handleCreateTheme();

		/**
		 * 处理扫描二维码返回
		 */
		void handleScanQRCode();

		/**
		 * 创建头脑风暴会议
		 */
		void createBrainStorm();

		/**
		 * 显示会议二维码
		 */
		void showQRCode(MeetingUserInfo meetingInfo);

		/**
		 * 退出会议
		 */
		void quitMeeting(MeetingUserInfo meetingInfo);

		/**
		 * 解散会议
		 */
		void dismissMeeting(MeetingUserInfo meetingInfo);

		/**
		 * 扫描二维码加入会议
		 */
		void joinMeetingByQRCode(String result);

		/**
		 * 点击列表加入会议
		 */
		void joinMeeting(MeetingUserInfo meetingInfo);

		/**
		 * 取消会议预约
		 */
		void cancelMeeting(MeetingUserInfo meetingInfo);

		/**
		 * 展示会议过程详情
		 */
		void showMeetingProcedure(MeetingUserInfo meetingInfo);


	}

	/**
	 * Model接口方法，一般是数据操作方法,不一定存在Model
	 */
	interface Model extends BaseModel {

		/**
		 * 获取会议详情
		 */
		void getMeetingDetail(int meetingId, ResponseHandlerInterface response);

		/**
		 * 获取当前登录人用户信息
		 */
		void getUserInfo(ResponseHandlerInterface response);
		/**
		 * 加载会议列表网络数据
		 */
		void loadMeetingList(ResponseHandlerInterface response);

		/**
		 * 用户登出
		 */
		void logout(ResponseHandlerInterface response);

		/**
		 * 加入会议
		 */
		void joinMeeting(MeetingInfo info, ResponseHandlerInterface response);

		/**
		 * 退出会议
		 */
		void quitMeeting(MeetingInfo info, ResponseHandlerInterface response);
		/**
		 * 取消会议
		 */
		void cancelMeeting(MeetingInfo info, ResponseHandlerInterface response);
		/**
		 * 解散会议
		 */
		void dismissMeeting(MeetingInfo info, ResponseHandlerInterface response);

		/**
		 * 请求创建头脑风暴
		 */
		void requestBrainStorm(ResponseHandlerInterface response);


	}

}
