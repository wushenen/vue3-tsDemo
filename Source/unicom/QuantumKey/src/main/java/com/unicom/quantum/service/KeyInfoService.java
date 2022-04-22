package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.DeviceKeyUsedInfoResponse;
import com.unicom.quantum.pojo.KeyInfo;

import java.util.List;
import java.util.Map;

public interface KeyInfoService {
    void addKeyInfo(byte[] keyId,byte[] keyValue,String applicant,int keyStatus) throws Exception;
    KeyInfo getKeyInfo(byte[] keyId, String deviceName) throws Exception;
    int updateKeyInfo(byte[] keyId,int keyStatus);
    void deleteKeyInfo(byte[] keyId);
    List<KeyInfo> getKeyInfos(String applicant, int keyStatus);
    List<KeyInfo> getKeyInfosNotInKeyStatus(String applicant, int keyStatus);

    Map<String,Long> keyInfoStatistics(String applicant);
    String getAdminEmail();

    Long getApplicantKeyNum(String applicant);

    List<DeviceKeyUsedInfoResponse> getDeviceKeyUsedInfo(String deviceName);
}
