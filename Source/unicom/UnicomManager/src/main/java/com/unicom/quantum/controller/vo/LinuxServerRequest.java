package com.unicom.quantum.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel("修改系统配置请求参数")
@Data
public class LinuxServerRequest {

    @ApiModelProperty("ip地址")
    @NotNull
    @Pattern(regexp = "(?=(\\b|\\D))(((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))$",
            message = "ip数据错误")
    private String ip;

    @ApiModelProperty("子网掩码")
    @Pattern(regexp = "^(254|252|248|240|224|192|128|0)\\.0\\.0\\.0|255\\.(254|252|248|240|224|192|128|0)\\.0\\.0|255\\.255\\.(254|252|248|240|224|192|128|0)\\.0|255\\.255\\.255\\.(254|252|248|240|224|192|128|0)$",
            message = "掩码数据错误")
    private String netMask;

    @ApiModelProperty("")
    @Pattern(regexp = "^(?=(\\b|\\D))(((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))$",
            message = "网关格式不正确")
    private String gateWay;

    @ApiModelProperty("网卡名称")
    @Pattern(regexp = "eth[0-3]" ,message = "网卡名字错误")
    private String nicName;

}




