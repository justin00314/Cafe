package com.ai2020lab.cafe.contract;

import com.ai2020lab.cafe.common.mvp.base.BaseModel;
import com.ai2020lab.cafe.common.mvp.base.BasePresenter;
import com.ai2020lab.cafe.common.mvp.base.BaseView;

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
		 * 设置用户信息显示
		 */
		void setUserInfo();

		void skipToMeetingDetailActivity();

	}

	/**
	 * Presenter接口方法，一般是Activity中的业务逻辑方法
	 */
	interface Presenter extends BasePresenter {

		/**
		 * 设置用户信息显示
		 */
		void setUserInfo();
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
		void createThemeMeeting();

		/**
		 * 创建头脑风暴会议
		 */
		void createBrainStormMeeting();

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

		/**
		 * 跳转到会议详情界面
		 */
		void skipToMeetingDetailActivity();


	}

	/**
	 * Model接口方法，一般是数据操作方法,不一定存在Model
	 */
	interface Model extends BaseModel {

	}

}
