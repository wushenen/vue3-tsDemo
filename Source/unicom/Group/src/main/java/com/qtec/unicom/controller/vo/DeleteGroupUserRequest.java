package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by jerry on 2020/11/27.
 */
@ApiModel(value = "删除分组用户请求参数")
@Data
public class DeleteGroupUserRequest {

    @ApiModelProperty(value = "终端id")
    private List<Integer> deviceId;

    @ApiModelProperty(value = "分组id")
    private int groupId;

}
