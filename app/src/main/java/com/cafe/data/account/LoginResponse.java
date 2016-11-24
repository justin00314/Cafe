package com.cafe.data.account;

import com.cafe.data.base.ResponseData;

/**
 * Created by Rocky on 2016/11/24.
 */

public class LoginResponse extends ResponseData<LoginResponse.TokenResponse> {

    public class TokenResponse {
        public String token;
    }
}
