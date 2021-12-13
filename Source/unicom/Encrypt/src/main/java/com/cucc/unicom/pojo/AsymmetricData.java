package com.cucc.unicom.pojo;

import java.io.Serializable;

public class AsymmetricData implements Serializable {
    private String algorithm;
    private String digest;
    private String keyId;
    private String keyVersionId;
    private String value;
    private String ciphertextBlob;
    private String plaintext;
    private String publicKey;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCiphertextBlob() {
        return ciphertextBlob;
    }

    public void setCiphertextBlob(String ciphertextBlob) {
        this.ciphertextBlob = ciphertextBlob;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
