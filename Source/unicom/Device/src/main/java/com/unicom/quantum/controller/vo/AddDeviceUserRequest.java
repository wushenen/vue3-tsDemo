package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "添加终端用户请求参数")
@Data
public class AddDeviceUserRequest {

    @ApiModelProperty(value = "用户名")
    private String deviceName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("备注")
    private String comments;

    @ApiModelProperty("用户类型（0-软件用户,1-硬件用户）")
    private int userType;
}
