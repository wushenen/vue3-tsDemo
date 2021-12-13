package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("添加IP请求参数")
public class IpInfoRequest {

    @ApiModelProperty(value = "ip地址")
    private String ipInfo;

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo;
    }
}
