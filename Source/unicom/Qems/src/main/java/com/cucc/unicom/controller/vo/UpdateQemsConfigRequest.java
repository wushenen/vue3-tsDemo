package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("修改配置请求参数")
@Data
public class UpdateQemsConfigRequest {

    @ApiModelProperty("配置id")
    private int id;
    @ApiModelProperty("配置名称")
    private String configName;
    @ApiModelProperty("加密端口")
    private String encPort;
    @ApiModelProperty("加密方式（0-不加密，1-AES，2-AES强制加密，6-SM4，7-SM4强制加密）")
    private int encType;
    @ApiModelProperty("密钥更新频率")
    private int encFreq;
    @ApiModelProperty("离线充注起始值")
    private Long startIndex;
    @ApiModelProperty("离线充注结束值")
    private Long endIndex;
}
