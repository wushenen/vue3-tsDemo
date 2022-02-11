package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "添加分组权限请求参数")
@Data
public class AddGroupAuthRequest {

    @ApiModelProperty(value = "分组id")
    private Integer groupId;

    @ApiModelProperty(value = "资源id")
    private List<Integer> apiId;
}
