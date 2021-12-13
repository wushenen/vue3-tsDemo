package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by jerry on 2020/11/27.
 */
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
