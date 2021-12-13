package com.cucc.unicom.pojo.DTO;

import lombok.Data;

@Data
public class ExportDeviceUserInfo {

    //对应表中的deviceName
    private String deviceId;

    private String password;

    //表中的是二进制，需要转为16进制导出
    private String encKey;

}
