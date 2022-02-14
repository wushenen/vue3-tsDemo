package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("KMS配置信息")
@Data
public class QKDConfig implements Serializable {
    @ApiModelProperty(value = "本端用户名",notes = "本端用户名")
    private String localName;
    @ApiModelProperty(value = "对端用户名",notes = "对端用户名")
    private String peerName;
    @ApiModelProperty(value = "设备密钥",notes = "设备密钥")
    private String devKey;
    @ApiModelProperty(value = "加密密钥",notes = "加密密钥")
    private String cryptKey;
}
