package com.cucc.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("修改邮件发件人请求参数")
@Data
public class MailConfigRequest {
    @ApiModelProperty("服务器地址")
    private String emailHost;
    @ApiModelProperty("发件人用户名")
    private String emailUsername;
    @ApiModelProperty("发件人密码/授权码")
    private String emailPassword;
    @ApiModelProperty("端口号")
    private Integer emailPort;
    @ApiModelProperty("协议")
    private String emailProtocol;
}
