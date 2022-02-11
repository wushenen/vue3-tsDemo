package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("应用登录请求参数")
@Data
public class AppLoginRequest {

    @ApiModelProperty(value = "appKey")
    private String appKey;

    @ApiModelProperty(value = "appSecret")
    private String appSecret;

}
