package com.cucc.unicom.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoleAuthInfo implements Serializable {

    private Integer roleAuthId;

    private Integer roleId;

    private String roleCode;

    private Integer apiId;
    private String apiName;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
