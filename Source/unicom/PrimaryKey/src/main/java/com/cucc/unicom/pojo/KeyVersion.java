package com.cucc.unicom.pojo;

import java.io.Serializable;
import java.util.Date;

public class KeyVersion implements Serializable {
    private String keyId;
    private String keyVersionId;
    private Date creationDate;
    private byte[] keyData;
    private byte[] priKeyData;
    private byte[] pubKeyData;
    private String creator;
    private Integer cardIndex;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public byte[] getKeyData() {
        return keyData;
    }

    public void setKeyData(byte[] keyData) {
        this.keyData = keyData;
    }

    public byte[] getPriKeyData() {
        return priKeyData;
    }

    public void setPriKeyData(byte[] priKeyData) {
        this.priKeyData = priKeyData;
    }

    public byte[] getPubKeyData() {
        return pubKeyData;
    }

    public void setPubKeyData(byte[] pubKeyData) {
        this.pubKeyData = pubKeyData;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }
}
