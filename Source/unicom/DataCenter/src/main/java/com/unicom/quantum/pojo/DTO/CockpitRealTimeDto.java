package com.unicom.quantum.pojo.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 云平台驾驶舱实时日活
 * @author lenovo
 */
@Data
@ApiModel("云平台驾驶舱实时日活")
public class CockpitRealTimeDto {

    @ApiModelProperty("时间")
    List<String> times;

    @ApiModelProperty("注册数量")
    List<String> registerNums;

    @ApiModelProperty("在线数量")
    List<String> onlineNums;

    @ApiModelProperty("注册总数量")
    String registerTotal;

    @ApiModelProperty("在线总数量")
    String onlineTotal;

    @ApiModelProperty("当日峰值")
    String peakDay;

    public CockpitRealTimeDto() {
        setTimes(new ArrayList<>());
        setRegisterNums(new ArrayList<>());
        setOnlineNums(new ArrayList<>());
    }
}
