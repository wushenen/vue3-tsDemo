package com.unicom.quantum.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceInfo implements Serializable {

    private Integer deviceId;

    private String deviceName;

    private String comments;
}
