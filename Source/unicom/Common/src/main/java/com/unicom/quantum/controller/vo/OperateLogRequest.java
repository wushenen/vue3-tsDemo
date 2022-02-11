package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("日志筛选请求参数")
public class OperateLogRequest {
    private String operator;
    private String operateModel;
    private String detail;
    private Date startTime;
    private Date endTime;
}
