package com.qtec.unicom.pojo.dto;


import java.io.Serializable;

public class SM9SignDTO  implements Serializable {
    private String keyId;
    //签名原文
    private String digest;
    //签名
    private String sign;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
