package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "添加分组应用用户请求参数")
@Data
public class AddGroupAppUserRequest {

    @ApiModelProperty(value = "应用用户id")
    private List<Integer> appUserId;

    @ApiModelProperty(value = "分组id")
    private int groupId;

}
