package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.KeyInfoMapper;
import com.cucc.unicom.mapper.KeyLimitMapper;
import com.cucc.unicom.mapper.KeyOfflineMapper;
import com.cucc.unicom.pojo.KeyInfo;
import com.cucc.unicom.service.KeyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyInfoServiceImpl implements KeyInfoService {

    @Autowired
    private KeyInfoMapper keyInfoMapper;

    @Autowired
    private KeyLimitMapper keyLimitMapper;

    @Autowired
    private KeyOfflineMapper keyOfflineMapper;

    @Override
    public void addKeyInfo(byte[] keyId,byte[] keyValue,String applicant, int keyStatus) {
        keyInfoMapper.addKeyInfo(keyId, keyValue, applicant,keyStatus);
    }

    @Override
    public KeyInfo getKeyInfo(byte[] keyId) {
        KeyInfo keyInfo = keyInfoMapper.getKeyInfo(keyId);
        return keyInfo;
    }

    @Override
    public int updateKeyInfo(byte[] keyId, int keyStatus) {
        keyInfoMapper.updateKeyInfo(keyId, keyStatus);
        return 0;
    }

    @Override
    public void deleteKeyInfo(byte[] keyId) {
        keyInfoMapper.deleteKeyInfo(keyId);
    }

    @Override
    public List<KeyInfo> getKeyInfos(String applicant,int keyStatus) {
        return keyInfoMapper.getKeyInfos(applicant,keyStatus);
    }

    @Override
    public List<KeyInfo> getKeyInfosNotInKeyStatus(String applicant, int keyStatus) {
        return keyInfoMapper.getKeyInfosNotInKeyStatus(applicant,keyStatus);
    }

    @Override
    public Map<String, Long> keyInfoStatistics(String applicant) {
        HashMap<String, Long> map = new HashMap<>();
        Long usedNum = keyInfoMapper.getDeviceStatusKeyNum(applicant);
        Long totalNum = keyLimitMapper.getKeyLimit(applicant);
        if (totalNum == null)
            totalNum = 0L;
        if (usedNum == null)
            usedNum = 0L;
        map.put("usedNum",usedNum);
        map.put("totalNum", totalNum);
        return map;
    }

    @Override
    public String getAdminEmail() {
        String adminEmail = keyInfoMapper.getAdminEmail();
        return adminEmail;
    }

    @Override
    public Long getApplicantKeyNum(String applicant) {
        Long totalNum = keyInfoMapper.getTotalNum(applicant);
        return totalNum;
    }
}
