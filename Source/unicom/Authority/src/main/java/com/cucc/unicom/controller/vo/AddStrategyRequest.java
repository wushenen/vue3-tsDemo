package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "添加策略信息请求参数")
@Data
public class AddStrategyRequest {

    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    @ApiModelProperty(value = "策略描述")
    private String strategyDescribe;

}
