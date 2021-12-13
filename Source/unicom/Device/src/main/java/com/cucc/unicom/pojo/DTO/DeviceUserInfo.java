package com.cucc.unicom.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
public class DeviceUserInfo implements Serializable {

    private int deviceId;

    private String deviceName;

    private String password;

    private String comments;

    private int userType;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Set<String> auth;
}
