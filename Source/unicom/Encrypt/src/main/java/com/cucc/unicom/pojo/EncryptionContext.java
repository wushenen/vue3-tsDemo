package com.cucc.unicom.pojo;

import java.io.Serializable;

public class EncryptionContext implements Serializable {
    private String keyId;
    private String keyVersionId;
    private String plaintext;

    private String encryptionContext;

    private String ciphertextBlob;

    //key_id+key_version+EncryptionContext  做的sm3
    private String keyHash;

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

    public String getEncryptionContext() {
        return encryptionContext;
    }

    public void setEncryptionContext(String encryptionContext) {
        this.encryptionContext = encryptionContext;
    }

    public String getKeyHash() {
        return keyHash;
    }

    public void setKeyHash(String keyHash) {
        this.keyHash = keyHash;
    }

    public String getCiphertextBlob() {
        return ciphertextBlob;
    }

    public void setCiphertextBlob(String ciphertextBlob) {
        this.ciphertextBlob = ciphertextBlob;
    }
}
