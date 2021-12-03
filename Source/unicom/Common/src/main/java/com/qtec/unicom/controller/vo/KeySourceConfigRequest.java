package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "量子密钥源配置请求参数")
@Data
public class KeySourceConfigRequest {

    @ApiModelProperty(value = "id",notes = "配置id")
    private int id;

    @ApiModelProperty(value = "密钥源IP",notes = "密钥源IP")
    private String SourceIp;

    @ApiModelProperty(value = "备用密钥源IP",notes = "备用密钥源IP")
    private String SourceIp2;



}
