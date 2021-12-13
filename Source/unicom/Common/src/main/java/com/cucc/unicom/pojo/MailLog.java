package com.cucc.unicom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MailLog {

    private int id;
    private String destination;
    private String detail;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private boolean mailStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(boolean mailStatus) {
        this.mailStatus = mailStatus;
    }

    public MailLog() {
    }

    public MailLog(int id, String destination, String detail, Date updateTime, boolean mailStatus) {
        this.id = id;
        this.destination = destination;
        this.detail = detail;
        this.updateTime = updateTime;
        this.mailStatus = mailStatus;
    }

    public MailLog(String destination, String detail, boolean mailStatus) {
        this.destination = destination;
        this.detail = detail;
        this.mailStatus = mailStatus;
    }
}
