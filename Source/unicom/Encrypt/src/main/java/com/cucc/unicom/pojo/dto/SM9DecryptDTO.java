package com.cucc.unicom.pojo.dto;

import java.io.Serializable;

public class SM9DecryptDTO implements Serializable {
    private String keyId;
    //加解密明文
    private String plaintext;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }
}
