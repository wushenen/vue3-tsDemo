package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "策略授权请求参数")
@Data
public class AddStrategyAuthorityRequest {

    @ApiModelProperty(value = "授权对象类型,1-终端用户;2-角色;3-分组,4-应用用户",required = true)
    private Integer authType;

    @ApiModelProperty(value = "终端id")
    private List<Integer> deviceId;

    @ApiModelProperty(value = "角色id")
    private List<Integer> roleId;

    @ApiModelProperty(value = "分组id")
    private List<Integer> groupId;

    @ApiModelProperty(value = "应用用户id")
    private List<Integer> appUserId;

    @ApiModelProperty(value = "策略id",required = true)
    private List<Integer> strategyId;

}
