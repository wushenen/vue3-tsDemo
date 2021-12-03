package com.qtec.unicom.pojo.dto;

import java.io.Serializable;

public class SM9EncryptDTO implements Serializable {
    private String keyId;
    //加解密密文
    private String ciphertextBlob;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getCiphertextBlob() {
        return ciphertextBlob;
    }

    public void setCiphertextBlob(String ciphertextBlob) {
        this.ciphertextBlob = ciphertextBlob;
    }
}
