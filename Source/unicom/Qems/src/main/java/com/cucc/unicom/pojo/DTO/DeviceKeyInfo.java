package com.cucc.unicom.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceKeyInfo implements Serializable {

    private Long keyNum;

    private String timeInfo;

}
