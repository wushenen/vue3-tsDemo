package com.qtec.unicom.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class StrategyActionInfo implements Serializable {

    private Integer strategyActionId;

    private Integer apiId;

    private String apiName;

    private String apiURL;

    private String comments;

}
