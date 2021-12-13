package com.cucc.unicom.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleInfo implements Serializable {

    private Integer roleId;

    private String roleCode;

    private String roleDescribe;

}
