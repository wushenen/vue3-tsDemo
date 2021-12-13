package com.cucc.unicom.pojo.dto;

public class MaterialDTO {
    private String importToken;
    private String keyId;
    private String publicKey;
    private String tokenExpireTime;

    public String getImportToken() {
        return importToken;
    }

    public void setImportToken(String importToken) {
        this.importToken = importToken;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(String tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    @Override
    public String toString() {
        return "MaterialDTO{" +
                "importToken='" + importToken + '\'' +
                ", keyId='" + keyId + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", tokenExpireTime='" + tokenExpireTime + '\'' +
                '}';
    }
}
