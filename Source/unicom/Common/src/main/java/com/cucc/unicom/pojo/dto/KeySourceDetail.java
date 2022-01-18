package com.cucc.unicom.pojo.dto;

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
    private String key_generate_rate;
    private Long key_generate_num;
}
