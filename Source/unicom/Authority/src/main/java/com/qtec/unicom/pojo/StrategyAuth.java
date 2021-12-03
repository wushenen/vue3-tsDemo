package com.qtec.unicom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StrategyAuth implements Serializable {

    private Integer strategyAuthId;

    private Integer deviceId;

    private Integer roleId;

    private Integer groupId;

    private Integer appUserId;

    private Integer strategyId;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
