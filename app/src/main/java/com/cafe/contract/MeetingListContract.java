package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;
import com.cafe.common.mvp.base.BaseView;
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

		void loadMeetingList(List<MeetingUserInfo> meetingInfoList);

		/**
		 * 设置用户信息显示
		 */
		void setUserInfo();

		/**
		 * 提示创建头脑风暴会议
		 */
		void promptShakePhone();

		/**
		 * 显示二维码对话框
		 */
		void showQRcodeDialog();

		/**
		 * 显示操作会议对话框
		 */
		void showOperatingMeetingDialog();

		void skipToMeetingDetailActivity();

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
		 * 创建头脑风暴会议
		 */
		void createBrainStorm();

		/**
		 * 解散会议
		 */
		void dismissMeeting();

		/**
		 * 加入会议
		 */
		void joinMeeting();

		/**
		 * 取消会议预约
		 */
		void cancelMeeting();


	}

	/**
	 * Model接口方法，一般是数据操作方法,不一定存在Model
	 */
	interface Model extends BaseModel {
		/**
		 * 加载会议列表网络数据
		 */
		void loadMeetingList(ResponseHandlerInterface response);

	}

}
