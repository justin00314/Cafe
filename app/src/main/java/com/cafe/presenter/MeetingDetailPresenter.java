package com.cafe.presenter;

import android.content.Context;

import com.cafe.common.mvp.MVPPresenter;
import com.cafe.contract.MeetingDetailContract;
import com.cafe.contract.MeetingListContract;
import com.cafe.data.meeting.MeetingUserInfo;

/**
 * Created by Justin Z on 2016/11/28.
 * 502953057@qq.com
 */

public class MeetingDetailPresenter extends MVPPresenter<MeetingListContract.View,
		MeetingListContract.Model> implements MeetingDetailContract.Presenter {

	private final static String TAG = MeetingDetailPresenter.class.getSimpleName();

	private Context context;

	public MeetingDetailPresenter(Context context){
		this.context = context;
	}

	@Override
	public void setMeetingInfo(MeetingUserInfo info) {

	}

	@Override
	public MeetingListContract.Model initModel() {
		return null;
	}
}
