package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "添加资源分类请求参数")
@Data
public class AddResourceCategoryRequest {

    @ApiModelProperty(value = "资源分类名称")
    private String apiName;

    @ApiModelProperty(value = "备注")
    private String comments;

}
