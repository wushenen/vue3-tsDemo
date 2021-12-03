package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "添加终端用户权限请求参数")
public class AddDeviceAuthorityRequest {

    @ApiModelProperty(value = "终端id")
    private Integer deviceId;

    @ApiModelProperty(value = "接口id")
    private List<Integer> apiId;
}
