package com.ai2020lab.cafe.presenter;

import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.MeetingSearchContract;

/**
 * Created by Rocky on 2016/11/11.
 */

public class MeetingSearchPresenter extends MVPPresenter<MeetingSearchContract.View, MeetingSearchContract.Model>
        implements MeetingSearchContract.Presenter<MeetingSearchContract.View, MeetingSearchContract.Model> {
    @Override
    public void search(String meetingId) {

    }

    @Override
    public MeetingSearchContract.Model initModel() {
        return null;
    }
}
