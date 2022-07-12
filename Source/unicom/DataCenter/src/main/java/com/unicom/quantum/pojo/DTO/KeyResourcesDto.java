package com.unicom.quantum.pojo.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 */
@Data
@ApiModel("密钥资源概览")
public class KeyResourcesDto {

    @ApiModelProperty("密钥消耗率")
    String keyConsumeRate;

    @ApiModelProperty("密钥消耗量")
    String consumption;

    @ApiModelProperty("密钥总量")
    String keyTotal;

    @ApiModelProperty("时间")
    List<String> times;

    @ApiModelProperty("密钥吞吐量发送次数")
    List<String> keyTpsSendTimes;

    @ApiModelProperty("密钥吞吐量接收次数")
    List<String> keyTpsRecTimes;

    @ApiModelProperty("设备资源")
    List<String> devResources;

    @ApiModelProperty("设备在线量")
    List<String> devOnlineNums;

    @ApiModelProperty("总量")
    List<String> devTotalNums;

    public KeyResourcesDto() {

        List<String> deviceList = new ArrayList<>();
        deviceList.add("密钥云终端");
        deviceList.add("安全路由器");

        setTimes(new ArrayList<>());
        setKeyTpsSendTimes(new ArrayList<>());
        setKeyTpsRecTimes(new ArrayList<>());
        setDevResources(deviceList);
        setDevOnlineNums(new ArrayList<>());
        setDevTotalNums(new ArrayList<>());
    }
}
