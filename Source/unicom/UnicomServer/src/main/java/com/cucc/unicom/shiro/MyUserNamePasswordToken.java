package com.cucc.unicom.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class MyUserNamePasswordToken extends UsernamePasswordToken {

    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public MyUserNamePasswordToken(String username, String password, String userType) {
        super(username, password);
        this.userType = userType;
    }
}
