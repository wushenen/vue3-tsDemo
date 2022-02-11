package com.unicom.quantum.pojo;

import java.io.Serializable;

public class Random implements Serializable {

    Integer passwordLength;

    private Integer keyLen;

    private String keyId;

    private byte[]tempKey;

    public Integer getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(Integer passwordLength) {
        this.passwordLength = passwordLength;
    }

    public Integer getKeyLen() {
        return keyLen;
    }

    public void setKeyLen(Integer keyLen) {
        this.keyLen = keyLen;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public byte[] getTempKey() {
        return tempKey;
    }

    public void setTempKey(byte[] tempKey) {
        this.tempKey = tempKey;
    }
}
