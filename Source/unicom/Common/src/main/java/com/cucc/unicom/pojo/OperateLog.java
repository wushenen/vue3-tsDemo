package com.cucc.unicom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OperateLog implements Serializable {
    private int id;
    private String operator;
    private String operateModel;
    private String detail;
    private String operateIp;
    private Object operateStatus;
    public long execTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
