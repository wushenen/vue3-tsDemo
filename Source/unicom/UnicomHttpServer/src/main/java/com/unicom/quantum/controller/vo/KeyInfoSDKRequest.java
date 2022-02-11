package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "获取keyValue请求参数")
@Data
public class KeyInfoSDKRequest {
    @ApiModelProperty("keyId")
    private String keyId;

    @ApiModelProperty("token")
    private String token;
}
