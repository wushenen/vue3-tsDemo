package com.qtec.unicom.shiro;

public enum UserType {

    SYSTEM_USER("systemUser"),
    APP_USER("appUser"),
    DEVICE_USER("deviceUser"),
    ;

    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    UserType(String userType) {
        this.userType = userType;
    }

    UserType() {
    }
}
