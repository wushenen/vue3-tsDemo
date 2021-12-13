package com.cucc.unicom.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class PrimaryKeyDTO implements Serializable {
    private String  keyId;
    private String  keySpec;
    private String  keyUsage;
    private String  origin;
    private String  protectionLevel;
//    @JSONField(deserializeUsing = MyDateSerializerAndDeserializer.class)
    private String  automaticRotation;
    private String  rotationInterval;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    private String  description;
    private String  keyState;
    private String  primaryKeyVersion;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date  deleteDate;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date  lastRotationDate;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date  nextRotationDate;
    private String  arn;
    private String creator;
    private Long materialExpireTime;

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

    public String getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(String keyUsage) {
        this.keyUsage = keyUsage;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProtectionLevel() {
        return protectionLevel;
    }

    public void setProtectionLevel(String protectionLevel) {
        this.protectionLevel = protectionLevel;
    }

    public String getAutomaticRotation() {
        return automaticRotation;
    }

    public void setAutomaticRotation(String automaticRotation) {
        this.automaticRotation = automaticRotation;
    }

    public String getRotationInterval() {
        return rotationInterval;
    }

    public void setRotationInterval(String rotationInterval) {
        this.rotationInterval = rotationInterval;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyState() {
        return keyState;
    }

    public void setKeyState(String keyState) {
        this.keyState = keyState;
    }

    public String getPrimaryKeyVersion() {
        return primaryKeyVersion;
    }

    public void setPrimaryKeyVersion(String primaryKeyVersion) {
        this.primaryKeyVersion = primaryKeyVersion;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Date getLastRotationDate() {
        return lastRotationDate;
    }

    public void setLastRotationDate(Date lastRotationDate) {
        this.lastRotationDate = lastRotationDate;
    }

    public Date getNextRotationDate() {
        return nextRotationDate;
    }

    public void setNextRotationDate(Date nextRotationDate) {
        this.nextRotationDate = nextRotationDate;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getMaterialExpireTime() {
        return materialExpireTime;
    }

    public void setMaterialExpireTime(Long materialExpireTime) {
        this.materialExpireTime = materialExpireTime;
    }
}
