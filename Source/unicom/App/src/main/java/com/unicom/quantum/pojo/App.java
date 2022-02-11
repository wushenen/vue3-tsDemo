package com.unicom.quantum.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class App implements Serializable {
    private int appId;
    private String appName;
    private int appType;
    private String appKey;
    private String appSecret;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String comments;
}
