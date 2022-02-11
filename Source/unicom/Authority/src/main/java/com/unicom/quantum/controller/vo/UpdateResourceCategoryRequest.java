package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "修改资源分类信息请求参数")
@Data
public class UpdateResourceCategoryRequest {

    @ApiModelProperty(value = "资源分类id")
    private Integer apiId;

    @ApiModelProperty(value = "资源分类名称")
    private String apiName;

    @ApiModelProperty(value = "备注")
    private String comments;

}
