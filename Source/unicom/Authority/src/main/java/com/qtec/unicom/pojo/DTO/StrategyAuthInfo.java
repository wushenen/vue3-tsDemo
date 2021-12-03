package com.qtec.unicom.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 策略授权详细信息
 * */

@Data
public class StrategyAuthInfo implements Serializable {

    private Integer strategyAuthId;

    private String strategyName;

    private DeviceInfo deviceInfo;

    private RoleInfo roleInfo;

    private GroupInfo groupInfo;

    private AppUserInfo appUserInfo;

    @JsonFormat(locale = "zh",timezone = "GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
