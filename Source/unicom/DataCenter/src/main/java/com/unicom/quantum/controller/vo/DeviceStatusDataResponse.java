package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("获取汇总信息返回结果")
@Data
@Accessors(chain = true)
public class DeviceStatusDataResponse {

    @ApiModelProperty("在线设备数量")
    private int onlineNum;

    @ApiModelProperty("设备总数量")
    private int deviceNum;

    @ApiModelProperty("离线设备数量")
    private int offlineNum;

    @ApiModelProperty("平台密钥分配总量")
    private Long keyDistributionNum;

    @ApiModelProperty(value = "已使用密钥总量",notes = "已使用密钥总量，包含撤回的密钥")
    private Long onlineKeyNum;

    @ApiModelProperty(value = "可使用密钥量",notes = "可使用密钥量，只包含能正常使用的量")
    private Long onlineEnableKeyNum;

    @ApiModelProperty("加密数据量")
    private Long encDataNum;

    @ApiModelProperty("解密数据量")
    private Long decDataNum;


}
