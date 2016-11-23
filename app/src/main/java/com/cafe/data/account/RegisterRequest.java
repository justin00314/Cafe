package com.cafe.data.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rocky on 2016/11/23.
 */

public class RegisterRequest {

    /**
     * 密码
     */
    @Expose
    public String password;
    /**
     * 用户名
     */
    @Expose
    @SerializedName("user_name")
    public String userName;
    /**
     * 员工工号
     */
    @Expose
    @SerializedName("job_number")
    public String workNumber;
    /**
     * 用户头像地址链接
     */
    @Expose
    @SerializedName("portrait")
    public String userPortrait;
}
