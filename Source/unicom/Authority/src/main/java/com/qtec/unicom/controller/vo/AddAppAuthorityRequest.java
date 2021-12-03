package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "添加应用用户权限请求参数")
public class AddAppAuthorityRequest {

    @ApiModelProperty(value = "应用用户id")
    private Integer appUserId;

    @ApiModelProperty(value = "接口id")
    private List<Integer> apiId;
}
