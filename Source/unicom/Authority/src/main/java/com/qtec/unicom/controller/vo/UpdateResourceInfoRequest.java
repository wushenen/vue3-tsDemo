package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "修改接口资源信息请求参数")
@Data
public class UpdateResourceInfoRequest {

    @ApiModelProperty(value = "接口资源id")
    private Integer apiId;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口URL")
    private String apiURL;

    @ApiModelProperty(value = "上级分类id")
    private Integer parentId;

    @ApiModelProperty(value = "备注")
    private String comments;

}
