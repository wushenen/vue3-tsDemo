package com.qtec.unicom.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeySourceConfig implements Serializable {

    private int id;
    private int keySource;
    private String SourceIp;
    private String SourceIp2;
    private String configInfo;
    private int priority;

}
