package com.cucc.unicom.pojo.dto;

public class DecryptionContextDTO {
    private String keyId;
    private String keyVersionId;
    private String plaintext;

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

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }
}
