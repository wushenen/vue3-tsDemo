package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("设备操作请求参数")
@Data
public class QemsOperateRequest {

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty(value = "操作",notes = "0-无操作;1-重启;2-置零")
    private int operation;

}
