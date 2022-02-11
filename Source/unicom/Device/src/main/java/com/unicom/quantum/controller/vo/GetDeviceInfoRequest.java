package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "获取终端用户信息")
@Data
public class GetDeviceInfoRequest {

    @ApiModelProperty(value = "用户id")
    private int deviceId;

}
