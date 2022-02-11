package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("删除终端请求参数")
@Data
public class DeleteDeviceRequest {

    @ApiModelProperty(value = "终端用户id")
    private List<Integer> deviceId;
}
