package com.cafe.presenter;

import android.content.Context;
import android.os.Handler;

import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.contract.ThemeMeetingCreateContract;
import com.cafe.data.meeting.CreateMeetingRequest;
import com.cafe.data.meeting.CreateMeetingResponse;
import com.cafe.model.meeting.BDKMeetingCreateBiz;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Rocky on 2016/11/10.
 */

public class ThemeMeetingCreatePresenter extends MVPPresenter<ThemeMeetingCreateContract.View, ThemeMeetingCreateContract.Model>
        implements ThemeMeetingCreateContract.Presenter<ThemeMeetingCreateContract.View, ThemeMeetingCreateContract.Model> {

    private Context mContext;

    public ThemeMeetingCreatePresenter(Context context) {
        super();
        mContext = context;

        mode = initModel();
    }

    @Override
    public ThemeMeetingCreateContract.Model initModel() {
        return new BDKMeetingCreateBiz(mContext);
    }

    @Override
    public void createNewMeeting(CreateMeetingRequest request) {
        getView().showLoadingProgress();

        getModel().createNewMeeting(request, new JsonHttpResponseHandler<CreateMeetingResponse>() {
            @Override
            public void onHandleSuccess(int statusCode, Header[] headers, final CreateMeetingResponse jsonObj) {
                if (getView() == null) {
                    // do nothing
                    return;
                }

                getView().dismissLoadingProgress();

                final boolean[] registerResult = {false};
                final long[] meetingId = {0};

                if (jsonObj != null && jsonObj.data.result) {
                    registerResult[0] = true;
                    meetingId[0] = jsonObj.data.id;

                }

                new Handler(mContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        getView().submitDone(registerResult[0], meetingId[0]);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (getView() != null) {
                    getView().dismissLoadingProgress();

                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            getView().submitDone(false, 0);
                        }
                    });
                }
            }
        });
    }
}
