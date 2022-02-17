package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("日志筛选请求参数")
public class OperateLogRequest {
    @ApiModelProperty("操作者")
    private String operator;
    @ApiModelProperty("操作模块")
    private String operateModel;
    @ApiModelProperty("操作功能")
    private String detail;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
}
