package com.cafe.presenter;

import android.content.Context;
import android.os.Handler;

import com.cafe.R;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.contract.MeetingSearchContract;
import com.cafe.data.meeting.JoinMeetingRequest;
import com.cafe.data.meeting.JoinMeetingResponse;
import com.cafe.data.meeting.MeetingInfo;
import com.cafe.data.meeting.MeetingListResponse;
import com.cafe.data.meeting.QueryMeetingResponse;
import com.cafe.model.meeting.BDKMeetingSearchBiz;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Rocky on 2016/11/11.
 */

public class MeetingSearchPresenter extends MVPPresenter<MeetingSearchContract.View, MeetingSearchContract.Model>
        implements MeetingSearchContract.Presenter<MeetingSearchContract.View, MeetingSearchContract.Model> {

    private Context mContext;

    public MeetingSearchPresenter(Context context) {
        super();
        mContext = context;

        setModel(initModel());
    }

    @Override
    public void search(int meetingId) {
        getView().showLoadingProgress(mContext.getString(R.string.searching));

        getModel().search(meetingId, new JsonHttpResponseHandler<QueryMeetingResponse>() {
            @Override
            public void onCancel() {
                super.onCancel();

                if (getView() == null)
                    return;

                getView().dismissLoadingProgress();
                getView().showNoNetworkPrompt();
            }

            @Override
            public void onHandleFailure(final String errorMsg) {

                if (getView() != null) {
                    getView().dismissLoadingProgress();

                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            getView().joinMeetingResult(false);
                        }
                    });
                }
            }

            @Override
            public void onHandleSuccess(int statusCode, Header[] headers, final QueryMeetingResponse jsonObj) {
                if (getView() == null) {
                    // do nothing
                    return;
                }

                getView().dismissLoadingProgress();

                final boolean[] result = {false};

                if (jsonObj != null && jsonObj.data != null) {
                    result[0] = true;
                }

                new Handler(mContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<MeetingInfo> meetings = new ArrayList<MeetingInfo>();
                        if (jsonObj.data.name != null) { // use name to judge meeting is whether exist
                            meetings.add(jsonObj.data);
                        }
                        getView().showSearchResults(result[0], meetings);
                    }
                });
            }
        });
    }

    @Override
    public void joinMeeting(JoinMeetingRequest request) {
        getModel().joinMeeting(request, new JsonHttpResponseHandler<JoinMeetingResponse>() {
            @Override
            public void onCancel() {
                super.onCancel();

                if (getView() == null)
                    return;

                getView().dismissLoadingProgress();
                getView().showNoNetworkPrompt();
            }

            @Override
            public void onHandleFailure(final String errorMsg) {

                if (getView() != null) {
                    getView().dismissLoadingProgress();

                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            getView().showSearchResults(false, null);
                        }
                    });
                }
            }

            @Override
            public void onHandleSuccess(int statusCode, Header[] headers, final JoinMeetingResponse jsonObj) {
                if (getView() == null) {
                    // do nothing
                    return;
                }

                getView().dismissLoadingProgress();

                final boolean[] result = {false};

                if (jsonObj != null && jsonObj.data.result) {
                    result[0] = true;
                }

                new Handler(mContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        getView().joinMeetingResult(result[0]);
                    }
                });
            }
        });
    }

    @Override
    public MeetingSearchContract.Model initModel() {
        return new BDKMeetingSearchBiz(mContext);
    }
}
