package com.qtec.unicom.pojo;

import java.io.Serializable;

public class DataKey implements Serializable {
    private String  keyId;
    private String  keySpec;
    private Integer  numberOfBytes;
    private String  encryptionContext;
    private String  ciphertextBlob;
    private String  keyVersionId;
    private String  plaintext;
    private String  publicKeyBlob;
    private String  wrappingAlgorithm;
    private String  wrappingKeySpec;
    private String  exportedDataKey;
    private String  destinationKeyId;
    private String  sourceKeyId;

    private String  sourceKeyVersionId;

    private String  sourceEncryptionAlgorithm;

    private String  sourceEncryptionContext;
    private String  destinationEncryptionContext;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeySpec() {
        return keySpec;
    }

    public void setKeySpec(String keySpec) {
        this.keySpec = keySpec;
    }

    public Integer getNumberOfBytes() {
        return numberOfBytes;
    }

    public void setNumberOfBytes(Integer numberOfBytes) {
        this.numberOfBytes = numberOfBytes;
    }

    public String getEncryptionContext() {
        return encryptionContext;
    }

    public void setEncryptionContext(String encryptionContext) {
        this.encryptionContext = encryptionContext;
    }

    public String getCiphertextBlob() {
        return ciphertextBlob;
    }

    public void setCiphertextBlob(String ciphertextBlob) {
        this.ciphertextBlob = ciphertextBlob;
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

    public String getPublicKeyBlob() {
        return publicKeyBlob;
    }

    public void setPublicKeyBlob(String publicKeyBlob) {
        this.publicKeyBlob = publicKeyBlob;
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

    public String getExportedDataKey() {
        return exportedDataKey;
    }

    public void setExportedDataKey(String exportedDataKey) {
        this.exportedDataKey = exportedDataKey;
    }

    public String getDestinationKeyId() {
        return destinationKeyId;
    }

    public void setDestinationKeyId(String destinationKeyId) {
        this.destinationKeyId = destinationKeyId;
    }

    public String getSourceKeyId() {
        return sourceKeyId;
    }

    public void setSourceKeyId(String sourceKeyId) {
        this.sourceKeyId = sourceKeyId;
    }

    public String getSourceKeyVersionId() {
        return sourceKeyVersionId;
    }

    public void setSourceKeyVersionId(String sourceKeyVersionId) {
        this.sourceKeyVersionId = sourceKeyVersionId;
    }

    public String getSourceEncryptionAlgorithm() {
        return sourceEncryptionAlgorithm;
    }

    public void setSourceEncryptionAlgorithm(String sourceEncryptionAlgorithm) {
        this.sourceEncryptionAlgorithm = sourceEncryptionAlgorithm;
    }

    public String getSourceEncryptionContext() {
        return sourceEncryptionContext;
    }

    public void setSourceEncryptionContext(String sourceEncryptionContext) {
        this.sourceEncryptionContext = sourceEncryptionContext;
    }

    public String getDestinationEncryptionContext() {
        return destinationEncryptionContext;
    }

    public void setDestinationEncryptionContext(String destinationEncryptionContext) {
        this.destinationEncryptionContext = destinationEncryptionContext;
    }
}
