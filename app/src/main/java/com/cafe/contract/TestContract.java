package com.cafe.contract;

import com.cafe.common.mvp.base.BaseModel;
import com.cafe.common.mvp.base.BasePresenter;
import com.cafe.common.mvp.base.BaseView;

/**
 * MVP接口协议类示例
 * Created by Justin Z on 2016/11/1.
 * 502953057@qq.com
 */

public interface TestContract {

	/**
	 * View的接口方法，由Activity去实现
	 */
	interface View extends BaseView {

	}

	/**
	 * Presenter接口方法，一般是Activity中的业务逻辑方法
	 */
	interface Presneter extends BasePresenter {

		void saveToDiskLruCache();

		void getFromDiskLruCache();

		void startService();

		void stopService();

	}

	/**
	 * Model接口方法，一般是数据操作方法,不一定存在Model
	 */
	interface Model extends BaseModel {

	}


}
