package com.cafe.data.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rocky on 2016/11/23.
 */

public class LoginRequest {

    public static final int DEVICE_MOBILE = 1;
    public static final int DEVICE_WEB = 2;

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
     * 设备类型
     */
    @Expose
    @SerializedName("device_type")
    public int deviceType = DEVICE_MOBILE;
}
