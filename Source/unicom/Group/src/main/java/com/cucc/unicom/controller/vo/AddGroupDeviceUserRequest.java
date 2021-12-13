package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "添加分组终端用户请求参数")
@Data
public class AddGroupDeviceUserRequest {

    @ApiModelProperty(value = "终端id")
    private List<Integer> deviceId;

    @ApiModelProperty(value = "分组id")
    private int groupId;

}
