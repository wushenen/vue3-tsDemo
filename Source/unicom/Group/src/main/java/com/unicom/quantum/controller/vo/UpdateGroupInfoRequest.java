package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "修改分组信息请求参数")
@Data
public class UpdateGroupInfoRequest {

    @ApiModelProperty(value = "分组id")
    private int groupId;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "分组描述")
    private String groupDescribe;
}
