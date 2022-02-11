package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("管理员管理应用请求参数")
@Data
public class UserAppRequest {
    @ApiModelProperty(value = "应用ID")
    private int appId;
    @ApiModelProperty(value = "用户ID")
    private List<Integer> userIds;
}
