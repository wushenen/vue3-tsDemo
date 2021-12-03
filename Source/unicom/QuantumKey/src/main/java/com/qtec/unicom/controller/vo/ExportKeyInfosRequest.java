package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("导出用户量子密钥请求参数")
@Data
public class ExportKeyInfosRequest {

    @ApiModelProperty("请求者")
    private String applicant;

}
