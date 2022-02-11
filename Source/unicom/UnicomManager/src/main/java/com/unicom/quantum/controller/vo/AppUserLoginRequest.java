package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("应用用户登录请求参数")
@Data
public class AppUserLoginRequest {

    @ApiModelProperty(value = "应用用户应用key")
    private String appKey;

    @ApiModelProperty(value = "应用用户密码")
    private String appSecret;

}
