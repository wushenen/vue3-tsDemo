package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("终端用户登录请求参数")
public class DeviceUserLoginRequest {

    @ApiModelProperty(value = "终端登录用户名")
    private String deviceName;

    @ApiModelProperty(value = "密码")
    private String password;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
