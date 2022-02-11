package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "上报终端运行数据请求参数")
@Data
public class UploadDataRequest {

    @ApiModelProperty("设备名称，即终端id")
    private String deviceName;

    @ApiModelProperty("配置版本信息")
    private int version;

    @ApiModelProperty("密钥使用量")
    private Long keyNum;

    @ApiModelProperty("加密数据量")
    private Long encData;

    @ApiModelProperty("解密数据量")
    private Long decData;

    @ApiModelProperty("加解密状态(1-AES,2-AES强制,6-SM4,7-SM4强制)")
    private int status;

}
