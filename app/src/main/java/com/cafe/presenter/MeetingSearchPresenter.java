package com.cafe.presenter;

import com.cafe.common.mvp.MVPPresenter;
import com.cafe.contract.MeetingSearchContract;

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
