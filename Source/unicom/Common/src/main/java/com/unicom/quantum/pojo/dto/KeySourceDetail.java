package com.unicom.quantum.pojo.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class KeySourceDetail implements Serializable {
    private int id;
    private int keySource;
    private String SourceIp;
    private String SourceIp2;
    private String configInfo;
    private int priority;
    private String keyGenerateRate;
    private Long keyGenerateNum;
}
