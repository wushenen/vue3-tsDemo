package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("获取临时密钥请求参数")
@Data
public class GenerateTempKeyRequest {

    @ApiModelProperty(value = "密钥长度")
    private Integer keyLen;

    @ApiModelProperty(value = "密钥id")
    private String keyId;

}
