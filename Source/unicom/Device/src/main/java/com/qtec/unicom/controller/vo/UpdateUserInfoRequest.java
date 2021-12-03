package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "修改终端用户信息请求参数")
@Data
public class UpdateUserInfoRequest {

    @ApiModelProperty(value = "id")
    private int deviceId;

    @ApiModelProperty(value = "用户名")
    private String deviceName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "备注信息")
    private String comments;
}
