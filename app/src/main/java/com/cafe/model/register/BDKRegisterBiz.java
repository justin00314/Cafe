package com.cafe.model.register;

import android.content.Context;

import com.cafe.common.net.HttpManager;
import com.cafe.common.net.JsonHttpResponseHandler;
import com.cafe.common.net.UrlName;
import com.cafe.contract.RegisterContract;
import com.cafe.data.account.RegisterRequest;
import com.cafe.data.base.ResultResponse;

/**
 * Created by Rocky on 2016/11/23.
 */

public class BDKRegisterBiz implements RegisterContract.Model {

    private Context mContext;

    public BDKRegisterBiz(Context context) {
        mContext = context;
    }

    @Override
    public void register(RegisterRequest request, String headFilePath, JsonHttpResponseHandler<ResultResponse> handler) {
        HttpManager.postFile(mContext, UrlName.USER_REGISTER.getUrl(), request, headFilePath, handler);
    }
}
