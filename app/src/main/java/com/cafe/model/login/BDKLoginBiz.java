package com.cafe.model.login;

import android.content.Context;

import com.cafe.common.net.HttpManager;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.UrlName;
import com.cafe.contract.LoginContract;
import com.cafe.data.account.LoginRequest;
import com.cafe.data.account.LoginResponse;

import org.justin.utils.common.JsonUtils;
import org.justin.utils.storage.PreferencesUtils;

/**
 * Created by Rocky on 2016/11/24.
 */

public class BDKLoginBiz implements LoginContract.Model {

    private Context mContext;

    public BDKLoginBiz(Context context) {
        mContext = context;
    }

    @Override
    public void login(LoginRequest request, JsonHttpResponseHandler<LoginResponse> handler) {
        String requestString = JsonUtils.getInstance().serializeToJson(request);
        HttpManager.postJson(mContext, UrlName.USER_LOGIN.getUrl(), requestString, handler);
    }

    @Override
    public void processToken(String token) {
        PreferencesUtils.setString(mContext, "KEY_TOKEN", token);
    }
}
