package com.qtec.unicom.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "添加系统用户请求参数")
@Data
public class AddUserRequest {

    @ApiModelProperty(value = "用户名" ,required = true)
    private String userName;

    @ApiModelProperty(value = "密码" ,required = true)
    private String password;

    @ApiModelProperty(value = "密码" ,required = true)
    private String email;

    @ApiModelProperty(value = "用户类型" ,required = true)
    private int accountType;

    @ApiModelProperty(value = "备注")
    private String comments;

}
