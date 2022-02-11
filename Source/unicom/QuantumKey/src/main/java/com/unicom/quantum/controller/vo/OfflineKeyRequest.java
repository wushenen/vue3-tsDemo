package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("离线充注密钥请求参数")
@Data
public class OfflineKeyRequest {

    @ApiModelProperty(value = "开始值",notes = "开始值")
    private Long startIndex;
    @ApiModelProperty(value = "结束值",notes = "结束值")
    private Long endIndex;

}
