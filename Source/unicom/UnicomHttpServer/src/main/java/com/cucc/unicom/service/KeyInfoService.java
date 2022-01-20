package com.cucc.unicom.service;

import com.cucc.unicom.pojo.KeyInfo;

import java.util.Map;

public interface KeyInfoService {
    void addKeyInfo(byte[] keyId, byte[] keyValue, String applicant, int keyStatus);
    KeyInfo getKeyInfo(byte[] keyId);
    int updateKeyInfo(byte[] keyId, int keyStatus);
    Map<String,Long> keyInfoStatistics(String applicant);
    String getAdminEmail();
}
