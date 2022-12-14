package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.util.DataTools;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.mapper.KeyInfoMapper;
import com.unicom.quantum.mapper.KeyLimitMapper;
import com.unicom.quantum.pojo.KeyInfo;
import com.unicom.quantum.service.KeyInfoService;
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
    @Autowired
    private UtilService utilService;


    @Override
    public void addKeyInfo(byte[] keyId,byte[] keyValue,String applicant, int keyStatus) throws Exception {
        keyInfoMapper.addKeyInfo(keyId, DataTools.encryptMessage(keyValue), applicant,keyStatus);
    }

    @Override
    public KeyInfo getKeyInfo(byte[] keyId, String deviceName) throws Exception {
        byte[] keyValue;
        KeyInfo keyInfo = keyInfoMapper.getKeyInfo(keyId);
        if (keyInfo == null) {
            KeyInfo keyInfo1 = new KeyInfo();
            keyValue = utilService.generateQuantumRandom(32);
            keyInfo1.setKeyId(keyId);
            keyInfo1.setKeyValue(keyValue);
            keyInfo1.setKeyStatus(2);
            keyInfoMapper.addKeyInfo(keyId,DataTools.encryptMessage(keyValue),deviceName,2);
            return keyInfo1;
        }else{
            keyInfo.setKeyValue(DataTools.decryptMessage(keyInfo.getKeyValue()));
            keyInfoMapper.updateKeyInfo(keyId,2);
            return keyInfo;
        }
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
