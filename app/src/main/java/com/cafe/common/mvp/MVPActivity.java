package com.cafe.common.mvp;

import android.os.Bundle;

import com.aiviews.dialog.AlertDialogInfo;
import com.aiviews.dialog.OnClickDialogBtnListener;
import com.cafe.R;
import com.cafe.base.AIBaseActivity;
import com.cafe.common.mvp.base.BaseView;
import com.cafe.common.mvp.base.IMVPPresent;

import org.justin.utils.common.ToastUtils;
import org.justin.utils.system.NetworkUtils;

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

		if (mPresenter != null) {
			// 这里一定是传入泛型
			mPresenter.attachView((V) this);
		}

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

		if (mPresenter != null) {
			mPresenter.detachView();
		}
	}

	@Override
	public void showLoadingProgress(String content) {
		if (content == null) {
			showLoading(getString(R.string.prompt_loading));
		} else {
			showLoading(content);
		}

	}

	@Override
	public void dismissLoadingProgress() {
		dismissLoading();
	}

	@Override
	public void showAlertDialog(AlertDialogInfo info, OnClickDialogBtnListener<Void> listener) {
		showAlertDialogFg(info, listener);
	}

	@Override
	public void showNoNetworkPrompt() {
		ToastUtils.getInstance().showToast(this, R.string.prompt_no_network);
	}
}
