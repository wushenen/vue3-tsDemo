package com.cucc.unicom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OperateLog {
    private int id;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    public String operator;
    public String operateModel;
    public String detail;
    public String operateIp;
    private Object operateStatus;
    public long execTime;
}
