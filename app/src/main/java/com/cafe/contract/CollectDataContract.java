package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;


/**
 * 数据采集服务接口协议类
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */

public interface CollectDataContract {


	interface Presenter extends BasePresenter {

		/**
		 * 采集音量大小
		 */
		void collectAudioDB();



	}

	/**
	 * Model接口方法，一般是数据操作方法,不一定存在Model
	 */
	interface Model extends BaseModel {

	}

}
