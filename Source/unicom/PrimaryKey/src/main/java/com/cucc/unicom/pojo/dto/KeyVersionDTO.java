package com.cucc.unicom.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class KeyVersionDTO implements Serializable {
    private String keyId;
    private String keyVersionId;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

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

}
