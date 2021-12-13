package com.cucc.unicom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by wuzh on 2020/2/19.
 * Description: 操作日志数据表
 */
public class OperateLog {
    private int id;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    public String operator;
//    public String operateMicro;
    public String operateModel;
    public String detail;
    public String operateIp;
    private Object operateStatus;
    public long execTime;
//    public String macAddr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateModel() {
        return operateModel;
    }

    public void setOperateModel(String operateModel) {
        this.operateModel = operateModel;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public Object getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(Object operateStatus) {
        this.operateStatus = operateStatus;
    }

    public long getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }
}
