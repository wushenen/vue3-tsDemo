package com.unicom.quantum.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppDeviceDTO implements Serializable {
    private int id;
    private int appId;
    private int appType;
    private int deviceId;
    private String deviceName;
}
