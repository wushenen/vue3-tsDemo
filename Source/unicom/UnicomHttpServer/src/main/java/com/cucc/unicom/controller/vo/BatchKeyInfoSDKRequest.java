package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "批量获取keyValue请求参数")
@Data
public class BatchKeyInfoSDKRequest {
    @ApiModelProperty("keyId")
    private List<String> keyIds;

    @ApiModelProperty("token")
    private String token;
}
