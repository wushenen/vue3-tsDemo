package com.qtec.unicom.pojo;

import java.io.Serializable;

public class Material implements Serializable {
    private String  keyId;
    private Long  keyMaterialExpireUnix;
    private String  wrappingAlgorithm;
    private String  wrappingKeySpec;
    private String  publicKey;
    private String  encryptedKeyMaterial;
    private String importToken;
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Long getKeyMaterialExpireUnix() {
        return keyMaterialExpireUnix;
    }

    public void setKeyMaterialExpireUnix(Long keyMaterialExpireUnix) {
        this.keyMaterialExpireUnix = keyMaterialExpireUnix;
    }

    public String getWrappingAlgorithm() {
        return wrappingAlgorithm;
    }

    public void setWrappingAlgorithm(String wrappingAlgorithm) {
        this.wrappingAlgorithm = wrappingAlgorithm;
    }

    public String getWrappingKeySpec() {
        return wrappingKeySpec;
    }

    public void setWrappingKeySpec(String wrappingKeySpec) {
        this.wrappingKeySpec = wrappingKeySpec;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptedKeyMaterial() {
        return encryptedKeyMaterial;
    }

    public void setEncryptedKeyMaterial(String encryptedKeyMaterial) {
        this.encryptedKeyMaterial = encryptedKeyMaterial;
    }
    public String getImportToken() {
        return importToken;
    }

    public void setImportToken(String importToken) {
        this.importToken = importToken;
    }

    @Override
    public String toString() {
        return "Material{" +
                "keyId='" + keyId + '\'' +
                ", materialExpiretime=" + keyMaterialExpireUnix +
                ", wrappingAlgorithm='" + wrappingAlgorithm + '\'' +
                ", wrappingKeySpec='" + wrappingKeySpec + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", encryptedkeyMaterial='" + encryptedKeyMaterial + '\'' +
                '}';
    }
}
