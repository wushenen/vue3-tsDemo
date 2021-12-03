package com.qtec.unicom.pojo;

import java.io.Serializable;

public class SM9Encrypt implements Serializable {
    private String keyId;
    private String keyVersionId;
    //加解密明文
    private String plaintext;
    //加解密密文
    private String ciphertextBlob;
    //签名原文
    private String digest;
    //签名
    private String sign;
    //验签结果
    private String value;
    /**
     * 管理端操作类型
     * 0-获取公开参数
     * 1-加密
     * 2-解密
     * 3-签名
     * 4-验签
     */
    private Integer type;
    //用户标识
    private String idb;

    public String getIdb() {
        return idb;
    }

    public void setIdb(String idb) {
        this.idb = idb;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getKeyVersionId() {
        return keyVersionId;
    }

    public void setKeyVersionId(String keyVersionId) {
        this.keyVersionId = keyVersionId;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }

    public String getCiphertextBlob() {
        return ciphertextBlob;
    }

    public void setCiphertextBlob(String ciphertextBlob) {
        this.ciphertextBlob = ciphertextBlob;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SM9Encrypt{" +
                "keyId='" + keyId + '\'' +
                ", keyVersionId='" + keyVersionId + '\'' +
                ", plaintext='" + plaintext + '\'' +
                ", ciphertextBlob='" + ciphertextBlob + '\'' +
                ", digest='" + digest + '\'' +
                ", sign=" + sign +
                ", value='" + value + '\'' +
                ", type=" + type +
                ", idb='" + idb + '\'' +
                '}';
    }
}
