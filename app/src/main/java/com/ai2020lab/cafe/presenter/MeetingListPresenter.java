package com.ai2020lab.cafe.presenter;

import android.content.Context;

import com.ai2020lab.aiutils.common.ToastUtils;
import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.MeetingListContract;

/**
 * 会议列表界面Presenter
 * Created by Justin Z on 2016/11/8.
 * 502953057@qq.com
 */

public class MeetingListPresenter extends MVPPresenter<MeetingListContract.View,
		MeetingListContract.Model> implements MeetingListContract.Presenter {

	private final static String TAG = MeetingListPresenter.class.getSimpleName();

	private Context context;

	public MeetingListPresenter(Context context) {
		this.context = context;
	}

	@Override
	public MeetingListContract.Model initModel() {
		return null;
	}

	/**
	 * 加载会议列表
	 */
	@Override
	public void loadMeetingList() {

	}

	@Override
	public void logout() {
		ToastUtils.getInstance().showToast(context, "登出系统");
	}

	@Override
	public void search() {
		ToastUtils.getInstance().showToast(context, "搜索");
	}

	@Override
	public void scanQRCode() {
		ToastUtils.getInstance().showToast(context, "扫描二维码");
	}

	@Override
	public void createTheme() {

	}

	@Override
	public void createBrainStorm() {

	}

	@Override
	public void dismissMeeting() {

	}

	@Override
	public void joinMeeting() {

	}

	@Override
	public void cancelMeeting() {

	}




}
