package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by jerry on 2020/11/26.
 */
@ApiModel(value = "添加角色授权终端用户请求参数")
@Data
public class AddRoleDeviceUserRequest {

    @ApiModelProperty(value = "角色id")
    private int roleId;

    @ApiModelProperty(value = "用户id")
    private List<Integer> deviceId;
}
