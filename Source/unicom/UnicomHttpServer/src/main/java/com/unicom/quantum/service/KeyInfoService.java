package com.unicom.quantum.service;

import com.unicom.quantum.pojo.KeyInfo;

import java.util.Map;

public interface KeyInfoService {
    void addKeyInfo(byte[] keyId, byte[] keyValue, String applicant, int keyStatus) throws Exception;
    KeyInfo getKeyInfo(byte[] keyId, String deviceName) throws Exception;
    int updateKeyInfo(byte[] keyId, int keyStatus);
    Map<String,Long> keyInfoStatistics(String applicant);
    String getAdminEmail();
}
