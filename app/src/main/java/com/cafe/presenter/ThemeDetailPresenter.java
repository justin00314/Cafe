package com.cafe.presenter;

import android.content.Context;

import com.cafe.common.mvp.MVPPresenter;
import com.cafe.contract.ThemeDetailContract;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.meeting.MeetingUserInfo;

/**
 * 主题会议详情Presenter
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class ThemeDetailPresenter extends MVPPresenter<MeetingListContract.View,
		MeetingListContract.Model> implements ThemeDetailContract.Presenter {

	private final static String TAG = ThemeDetailPresenter.class.getSimpleName();

	private Context context;

	public ThemeDetailPresenter(Context context){
		this.context = context;
		setModel(initModel());
	}


	@Override
	public MeetingListContract.Model initModel() {
		return null;
	}

	@Override
	public void getNowTalker(MeetingUserInfo info) {

	}

	@Override
	public void loadProcedureList(MeetingUserInfo info) {

	}

	@Override
	public void startTheme(MeetingUserInfo info) {

	}

	@Override
	public void stopTheme(MeetingUserInfo info) {

	}

	@Override
	public void startEpisode(MeetingUserInfo info) {

	}

	@Override
	public void stopEpisode(MeetingUserInfo info) {

	}
}
