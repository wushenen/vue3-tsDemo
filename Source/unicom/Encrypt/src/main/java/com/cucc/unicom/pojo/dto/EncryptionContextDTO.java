package com.cucc.unicom.pojo.dto;


public class EncryptionContextDTO {
    private String keyId;
    private String keyVersionId;
    private String ciphertextBlob;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyVersionId() {
        return keyVersionId;
    }

    public void setKeyVersionId(String keyVersionId) {
        this.keyVersionId = keyVersionId;
    }

    public String getCiphertextBlob() {
        return ciphertextBlob;
    }

    public void setCiphertextBlob(String ciphertextBlob) {
        this.ciphertextBlob = ciphertextBlob;
    }
}
