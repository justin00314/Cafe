/*
 * Copyright (c) 2016. Justin Z All rights Reserved
 */

package com.cafe.base;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.aiviews.dialog.AlertDialog;
import com.aiviews.dialog.AlertDialogInfo;
import com.aiviews.dialog.LoadingDialog;
import com.aiviews.dialog.OnClickDialogBtnListener;
import com.aiviews.popupview.PromptView;
import com.aiviews.toolbar.ToolbarActivity;
import com.cafe.R;

import org.justin.utils.common.LogUtils;
import org.justin.utils.common.ResourcesUtils;
import org.justin.utils.common.ToastUtils;
import org.justin.utils.common.ViewUtils;
import org.justin.utils.system.DisplayUtils;
import org.justin.utils.system.NetworkUtils;

/**
 * Activity基类，继承自ToolbarActivity，添加网络连接监听，并且可以弹出进度条对话框
 * Created by Justin on 2015/12/10.
 * Email:502953057@qq.com,zhenghx3@asiainfo.com
 */
public class AIBaseActivity extends ToolbarActivity {

	private final static String TAG = AIBaseActivity.class.getSimpleName();

	/**
	 * 加载对话框TAG
	 */
	private final static String TAG_DIALOG_LOADING = "tag_dialog_loading";

	private final static String TAG_DIALOG_ALERT = "tag_dialog_alert";

	private LoadingDialog loadingDialog;

	private PromptView checkNetPromptView;

	/**
	 * 监听网络连接状态变化广播接收器
	 */
	private BroadcastReceiver netChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 网络状态改变
			onNetworkChange(NetworkUtils.getNetworkType(getActivity()));
		}
	};

	/**
	 * 程序入口
	 *
	 * @param savedInstanceState Bundle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		// 将每个Activity入栈
		ActivityManager.getInstance().add(this);
	}

	// 初始化数据和界面布局
	private void init() {
		Handler handler = new Handler();
		// 延迟500毫秒注册网络状态监听广播，否则弹出popupWindow会出错
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				registerNetReceiver(getActivity());
			}
		}, 500);
	}

	/**
	 * 显示加载对话框
	 *
	 * @param content 加载提示
	 */
	public void showLoading(String content) {
		if (loadingDialog == null) {
			loadingDialog = LoadingDialog.newInstance(content);
			Fragment fragment = getFragmentManager().findFragmentByTag(TAG_DIALOG_LOADING);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			if (fragment != null)
				ft.remove(fragment);
			ft.addToBackStack(null);// 加入回退栈
			loadingDialog.show(ft, TAG_DIALOG_LOADING);
		} else {
			loadingDialog.setMessage(content);
		}
	}

	/**
	 * 关闭进度条对话框
	 */
	public void dismissLoading() {
		LogUtils.i(TAG, "关闭进度条对话框");
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}

	/**
	 * 显示警告对话框
	 */
	public void showAlertDialogFg(AlertDialogInfo info, OnClickDialogBtnListener listener) {
		AlertDialog alertDialog = AlertDialog.newInstance(info.title, info.content,
				info.ensureText, info.cancelText);
		Fragment fragment = getFragmentManager().findFragmentByTag(TAG_DIALOG_ALERT);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (fragment != null)
			ft.remove(fragment);
		ft.addToBackStack(null);// 加入回退栈
		alertDialog.show(ft, TAG_DIALOG_ALERT);
		alertDialog.setOnClickDialogBtnListener(listener);
	}

	/**
	 * 注册广播
	 */
	private void registerNetReceiver(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(netChangeReceiver, intentFilter);
	}

	/**
	 * 解除广播
	 */
	private void unRegisterNetReceiver(Context context) {
		if (netChangeReceiver == null) return;
		context.unregisterReceiver(netChangeReceiver);
	}

	/**
	 * 监听网络状态改变<p>
	 * <strong>注意：子类覆盖这个方法的时候必须调用super.onNetworkChange(),否则网络无法连接提示不会弹出</strong>
	 *
	 * @param netType NetworkUtils.NetworkType
	 */
	protected void onNetworkChange(NetworkUtils.NetworkType netType) {
		LogUtils.i(TAG, "当前网络状态->" + netType.getName());
		if (!NetworkUtils.isNetworkAvailable(getActivity())) {
			LogUtils.i(TAG, "网络不可用");
			showNetPrompt();
		} else {
			LogUtils.i(TAG, "网络可用");
			dismissCheckNetPrompt();
		}
//		switch (netType) {
//			case UNKNOWN:
//			case DISCONNECTED:
//				showNetPrompt();
//				break;
//			case NET_2G:
//			case NET_3G:
//			case NET_4G:
//			case NET_WIFI:
//				dismissCheckNetPrompt();
//				break;
//		}
	}

	/**
	 * 显示网络出错提示
	 */
	private void showNetPrompt() {
		if (getToolbarVisibility()) {
			showCheckNetPrompt();
			return;
		}
		ToastUtils.getInstance().showToast(getActivity(),
				getString(R.string.prompt_check_network));
	}

	/**
	 * 显示网络出错提示
	 */
	private void showCheckNetPrompt() {
		View contentView = ViewUtils.makeView(getActivity(), R.layout.promptview_network_check);
		if (checkNetPromptView == null) {
			checkNetPromptView = new PromptView.Builder(contentView,
					DisplayUtils.getScreenWidth(getActivity()), 0)
					.setBackgroundDrawable(ResourcesUtils.getDrawable(R.drawable.popupview_bg))
					.create();
		}
		checkNetPromptView.showAsDropDown(getToolbar());
//		checkNetPromptView.showAtLocation();
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NetworkUtils.showWirelessSettings(getActivity());
			}
		});

	}

	/**
	 * 隐藏网络检查提示View
	 */
	private void dismissCheckNetPrompt() {
		if (checkNetPromptView != null && getToolbarVisibility()) {
			checkNetPromptView.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterNetReceiver(this);
	}


}
