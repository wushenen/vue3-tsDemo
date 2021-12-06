package com.qtec.unicom.service;

import com.qtec.unicom.pojo.KeyInfo;

import java.util.List;
import java.util.Map;

public interface KeyInfoService {
    void addKeyInfo(byte[] keyId,byte[] keyValue,String applicant,int keyStatus);
    KeyInfo getKeyInfo(byte[] keyId);
    int updateKeyInfo(byte[] keyId,int keyStatus);
    void deleteKeyInfo(byte[] keyId);
    List<KeyInfo> getKeyInfos(String applicant, int keyStatus);
    List<KeyInfo> getKeyInfosNotInKeyStatus(String applicant, int keyStatus);

    Map<String,Long> keyInfoStatistics(String applicant);
    String getAdminEmail();

    Long getApplicantKeyNum(String applicant);
}
