package com.cafe.common.mvp.base;

import com.aiviews.dialog.AlertDialogInfo;
import com.aiviews.dialog.OnClickDialogBtnListener;

/**
 * View基础接口类
 * Created by Justin Z on 2016/10/14.
 * 502953057@qq.com
 */
public interface BaseView {
	/**
	 * 显示加载进度条
	 * @param content 进度条内容，如果为null则显示默认内容
     */
	void showLoadingProgress(String content);

	/**
	 * 隐藏加载进度条
	 */
	void dismissLoadingProgress();

	/**
	 * 显示警告对话框
	 */
	void showAlertDialog(AlertDialogInfo info, OnClickDialogBtnListener<Void> listener);

	void showNoNetworkPrompt();
}
