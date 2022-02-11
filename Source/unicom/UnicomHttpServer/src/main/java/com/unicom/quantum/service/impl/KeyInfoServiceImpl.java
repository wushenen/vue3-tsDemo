package com.unicom.quantum.service.impl;

import com.unicom.quantum.mapper.KeyLimitMapper;
import com.unicom.quantum.pojo.KeyInfo;
import com.unicom.quantum.service.KeyInfoService;
import com.unicom.quantum.mapper.KeyInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KeyInfoServiceImpl implements KeyInfoService {

    @Autowired
    private KeyInfoMapper keyInfoMapper;
    @Autowired
    private KeyLimitMapper keyLimitMapper;


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

}
