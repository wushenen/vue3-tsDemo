package com.cucc.unicom.pojo.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class AppBaseInfo implements Serializable {

    private int appId;
    private String appName;
    private int appType;

}
