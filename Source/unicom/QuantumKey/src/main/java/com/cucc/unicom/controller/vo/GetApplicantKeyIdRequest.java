package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("获取用户keyId数据")
@Data
public class GetApplicantKeyIdRequest {

    @ApiModelProperty("用户名")
    private String applicant;
}
