package com.qtec.unicom.pojo.dto;

import java.io.Serializable;

public class SM9VeritySignDTO implements Serializable {
    private String keyId;
    //验签结果
    private String value;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
