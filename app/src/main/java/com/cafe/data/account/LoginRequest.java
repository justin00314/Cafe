package com.cafe.data.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rocky on 2016/11/23.
 */

public class LoginRequest {

    /**
     * 密码
     */
    @Expose
    public String password;
    /**
     * 用户名
     */
    @Expose
    @SerializedName("username")
    public String userName;
    /**
     * 设备类型
     */
    @Expose
    @SerializedName("device_type")
    public String deviceType;
}
