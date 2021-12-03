package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("批量导出终端用户请求参数")
@Data
public class ExportDeviceUserRequest {
    @ApiModelProperty("终端ids")
    List<Integer> deviceIds;
}
