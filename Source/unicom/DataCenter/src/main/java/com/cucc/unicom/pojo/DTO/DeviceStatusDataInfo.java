package com.cucc.unicom.pojo.DTO;

import lombok.Data;

/**
 * 数据总量
 */
@Data
public class DeviceStatusDataInfo {

    private int onlineNum;

    private int deviceNum;

    private int offlineNum;

    private Long keyNum;

    private Long encDataNum;

    private Long decDataNum;

/*    private int avgFre;

    private int maxFre;

    private int minFre;*/

}
