package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("终端密钥使用详情数据返回结果")
@Data
public class DeviceKeyUsedInfoResponse {

    @ApiModelProperty("密钥Id")
    private String keyId;

    @ApiModelProperty("申请密钥时间")
    private Date createTime;

}
