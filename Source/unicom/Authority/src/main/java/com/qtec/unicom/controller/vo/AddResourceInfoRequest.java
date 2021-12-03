package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "添加接口资源信息请求参数")
@Data
public class AddResourceInfoRequest {

    @ApiModelProperty(value = "接口资源名称")
    private String apiName;

    @ApiModelProperty(value = "接口URL")
    private String apiURL;

    @ApiModelProperty(value = "上级资源id")
    private Integer parentId;

    @ApiModelProperty(value = "备注")
    private String comments;

}
