package com.ai2020lab.cafe.fragment;

import android.app.DialogFragment;

/**
 * Created by Justin Z on 2016/11/11.
 * 502953057@qq.com
 */

public interface OnClickDialogBtnListener<T> {
	/**
	 * 点击确认按钮
	 *
	 * @param df DialogFragment
	 */
	void onClickEnsure(DialogFragment df, T t);

	/**
	 * 点击取消按钮
	 *
	 * @param df DialogFragment
	 */
	void onClickCancel(DialogFragment df);

}
