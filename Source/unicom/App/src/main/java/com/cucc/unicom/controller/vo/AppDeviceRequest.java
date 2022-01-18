package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("应用绑定终端请求参数")
@Data
public class AppDeviceRequest {
    @ApiModelProperty(value = "应用ID")
    private int appId;
    @ApiModelProperty(value = "终端ID")
    private List<Integer> deviceIds;
}
