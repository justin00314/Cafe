package com.cafe.presenter;

import android.content.Context;
import android.os.Handler;

import com.cafe.R;
import com.cafe.common.mvp.MVPPresenter;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.contract.LoginContract;
import com.cafe.data.account.LoginRequest;
import com.cafe.data.account.LoginResponse;
import com.cafe.model.login.BDKLoginBiz;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Rocky on 2016/11/11.
 */

public class LoginPresenter extends MVPPresenter<LoginContract.View, LoginContract.Model>
        implements LoginContract.Presenter<LoginContract.View, LoginContract.Model> {

    private Context mContext;

    public LoginPresenter(Context context) {
        super();
        mContext = context;

        setModel(initModel());
    }

    @Override
    public LoginContract.Model initModel() {
        return new BDKLoginBiz(mContext);
    }

    @Override
    public void login(LoginRequest request) {
        getView().showLoadingProgress(mContext.getString(R.string.submitting));

        getModel().login(request, new JsonHttpResponseHandler<LoginResponse>() {

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
                            getView().loginDone(false, errorMsg);
                        }
                    });
                }
            }

            @Override
            public void onHandleSuccess(int statusCode, Header[] headers, LoginResponse jsonObj) {
                if (getView() == null) {
                    // do nothing
                    return;
                }

                getView().dismissLoadingProgress();

                final boolean[] result = {false};

                if (jsonObj != null && jsonObj.data.token != null) {
                    result[0] = true;
                    getModel().processToken(jsonObj.data.token);

                }

                new Handler(mContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        getView().loginDone(result[0], "");
                    }
                });

            }
        });
    }
}
