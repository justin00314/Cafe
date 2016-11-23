package com.cafe.data.base;

import com.google.gson.annotations.Expose;

/**
 * Created by Rocky on 2016/11/23.
 */

public class ResultResponse extends ResponseData<ResultResponse.Result> {

    public class Result {
        @Expose
        public boolean result;
    }
}
