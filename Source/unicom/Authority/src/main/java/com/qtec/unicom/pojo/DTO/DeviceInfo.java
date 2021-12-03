package com.qtec.unicom.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceInfo implements Serializable {

    private Integer deviceId;

    private String deviceName;

    private String comments;
}
