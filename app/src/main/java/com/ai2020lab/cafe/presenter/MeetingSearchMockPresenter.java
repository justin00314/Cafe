package com.ai2020lab.cafe.presenter;

import com.ai2020lab.cafe.common.mvp.MVPPresenter;
import com.ai2020lab.cafe.contract.MeetingSearchContract;
import com.ai2020lab.cafe.data.meeting.MeetingInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rocky on 2016/11/11.
 */

public class MeetingSearchMockPresenter extends MVPPresenter<MeetingSearchContract.View, MeetingSearchContract.Model>
        implements MeetingSearchContract.Presenter<MeetingSearchContract.View, MeetingSearchContract.Model> {
    @Override
    public void search(String meetingId) {
        List<MeetingInfo> meetings = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            MeetingInfo meeting = new MeetingInfo();
            meeting.address = "address" + i;
            meeting.id = "" + new Random().nextInt(999999);
            meeting.name = "meeting " + i;
            meeting.startTime = "2016/" +  new Random().nextInt(12) + "/" + new Random().nextInt(28);

            meetings.add(meeting);
        }

        getView().showSearchResults(meetings);
    }

    @Override
    public MeetingSearchContract.Model initModel() {
        return null;
    }
}
