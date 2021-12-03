package com.qtec.unicom.pojo.dto;

public class ExportDataKeyDTO {
    private String keyId;
    private String keyVersionId;
    private String exportedDataKey;

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

    public String getExportedDataKey() {
        return exportedDataKey;
    }

    public void setExportedDataKey(String exportedDataKey) {
        this.exportedDataKey = exportedDataKey;
    }
}
