package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("添加应用请求参数")
@Data
public class AddAppRequest {
    @ApiModelProperty(value = "应用名称")
    private String appName;
    @ApiModelProperty(value = "应用类型",notes = "应用类型（1-专用应用；2-通用应用")
    private int appType;
    @ApiModelProperty(value = "备注信息")
    private String comments;
}
