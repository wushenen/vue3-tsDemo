package com.unicom.quantum.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupInfo implements Serializable {

    private Integer groupId;

    private String groupCode;

    private String groupName;

    private String groupDescribe;

}
