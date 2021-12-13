package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "添加策略操作请求参数")
@Data
public class AddStrategyActionRequest {

    @ApiModelProperty(value = "策略id")
    private Integer strategyId;

    @ApiModelProperty(value = "接口id")
    private List<Integer> apiId;

}
