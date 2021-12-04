package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("QKD参数修改请求参数")
@Data
public class UpdateQKDRequest {

    @ApiModelProperty(value = "主配置信息",notes = "QKD主配置信息")
    QKDConfig config;
    @ApiModelProperty(value = "备用配置信息",notes = "QKD备用配置信息")
    QKDConfig config2;

}
