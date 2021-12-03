package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "量子密钥源配置请求参数")
@Data
public class KeySourceConfigRequest {

    @ApiModelProperty(value = "密钥源",notes = "密钥源（1-QRNG，2-密码卡，3-902, 4-QKD）")
    private int keySource;

    @ApiModelProperty(value = "密钥源IP",notes = "密钥源IP")
    private String SourceIp;

    @ApiModelProperty(value = "备用密钥源IP",notes = "备用密钥源IP")
    private String SourceIp2;

    @ApiModelProperty(value = "优先级",notes = "1-4，1优先级最高")
    private int priority;

}
