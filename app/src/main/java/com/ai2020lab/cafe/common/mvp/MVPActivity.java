package com.ai2020lab.cafe.common.mvp;

import android.os.Bundle;

import com.ai2020lab.cafe.R;
import com.ai2020lab.cafe.base.AIBaseActivity;
import com.ai2020lab.cafe.common.mvp.base.BaseView;
import com.ai2020lab.cafe.common.mvp.base.IMVPPresent;

/**
 * Created by Justin Z on 2016/10/14.
 * 502953057@qq.com
 */
public abstract class MVPActivity<V extends BaseView, P extends IMVPPresent>
		extends AIBaseActivity implements BaseView {

	public P mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMVP();
	}

	@SuppressWarnings("unchecked")
	public void initMVP() {
		mPresenter = initPresenter();
		// 这里一定是传入泛型
		mPresenter.attachView((V) this);
	}

	/**
	 * 实例化Presenter
	 */
	public abstract P initPresenter();

	public P getPresenter() {
		return mPresenter;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPresenter.detachView();
	}

	@Override
	public void showLoadingProgress() {
		showLoading(getString(R.string.prompt_loading));
	}

	@Override
	public void dismissLoadingProgress() {
		dismissLoading();
	}

}
