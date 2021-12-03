package com.qtec.unicom.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShiroUser implements Serializable {

    private Object user;
    private String userType;

}
