package com.ai2020lab.cafe.common.mvp;

import android.app.Service;

import com.ai2020lab.cafe.common.mvp.base.BaseView;

/**
 * Created by Justin Z on 2016/11/2.
 * 502953057@qq.com
 */

public abstract class MVPService<P extends MVPPresenter> extends Service {

	public P mPresenter;

	@Override
	public void onCreate() {
		super.onCreate();
		initMVP();
	}

	@SuppressWarnings("unchecked")
	public void initMVP() {
		mPresenter = initPresenter();
//		// 这里一定是传入泛型
//		mPresenter.attachView((V) this);
	}

	/**
	 * 实例化Presenter
	 */
	public abstract P initPresenter();

	public P getPresenter() {
		return mPresenter;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		mPresenter.detachView();
	}
}
