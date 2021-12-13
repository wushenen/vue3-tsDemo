package com.cucc.unicom.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoleDeviceUserInfo implements Serializable {

    private int id;

    private int deviceId;

    private String deviceName;

    private String comments;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
