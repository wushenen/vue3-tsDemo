package com.unicom.quantum.pojo.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("主要资源概览")
@Data
public class MainResourceDto {

    @ApiModelProperty("资源")
    List<String> resources;

    @ApiModelProperty("总数")
    List<String> totals;

    @ApiModelProperty("开机")
    List<String> boots;

    @ApiModelProperty("关机")
    List<String> shutdowns;

    @ApiModelProperty("其他")
    List<String> others;

    public MainResourceDto() {
        List<String> resourceList = new ArrayList<>();
        resourceList.add("集群");
        resourceList.add("物理机");
        resourceList.add("存储");
        resourceList.add("云主机");

        setResources(resourceList);
        setTotals(new ArrayList<>());
        setBoots(new ArrayList<>());
        setShutdowns(new ArrayList<>());
        setOthers(new ArrayList<>());
    }
}
