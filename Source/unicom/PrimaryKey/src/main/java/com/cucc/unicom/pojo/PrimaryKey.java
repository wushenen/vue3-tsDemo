package com.cucc.unicom.pojo;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

public class PrimaryKey implements Serializable {
    private String  keyId;
    private String  keySpec;
    private String  keyUsage;
    private String  origin;
    private String  protectionLevel;
    private boolean  enableAutomaticRotation;
    private String automaticRotation;
    private String  rotationInterval;
    private Date creationDate;
    private String  description;
    private String  keyState;
    private String  primaryKeyVersion;
    private Date  deleteDate;
    private Date  lastRotationDate;
    private String  arn;
    private Integer pendingWindowInDays;
    private String creator;
    private String owner;


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

    public boolean isEnableAutomaticRotation() {
        return enableAutomaticRotation;
    }

    public void setEnableAutomaticRotation(boolean enableAutomaticRotation) {
        this.enableAutomaticRotation = enableAutomaticRotation;
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

    public Date getLastRotationDate() {
        return lastRotationDate;
    }

    public void setLastRotationDate(Date lastRotationDate) {
        this.lastRotationDate = lastRotationDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }


    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public Integer getPendingWindowInDays() {
        return pendingWindowInDays;
    }

    public void setPendingWindowInDays(Integer pendingWindowInDays) {
        this.pendingWindowInDays = pendingWindowInDays;
    }

    public String getAutomaticRotation() {
        return automaticRotation;
    }

    public void setAutomaticRotation(String automaticRotation) {
        this.automaticRotation = automaticRotation;
    }

    @Transient
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    @Transient
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
