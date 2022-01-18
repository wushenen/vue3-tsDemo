package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("取消角色用户授权请求参数")
@Data
public class CancelGroupDeviceUserRequest {

    @ApiModelProperty("分组用户信息ids")
    private List<Integer> ids;
}
