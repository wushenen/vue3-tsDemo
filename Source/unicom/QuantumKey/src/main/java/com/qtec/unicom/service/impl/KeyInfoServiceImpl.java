package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.KeyInfoMapper;
import com.qtec.unicom.mapper.KeyOfflineMapper;
import com.qtec.unicom.pojo.KeyInfo;
import com.qtec.unicom.service.KeyInfoService;
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
    public Map<String, Long> keyInfoStatistics(String applicant) {
        HashMap<String, Long> map = new HashMap<>();
        Long usedNum = keyInfoMapper.getUsedNum(applicant);
        Long totalNum = keyInfoMapper.getTotalNum(applicant);
        Long offlineKeyNum = keyOfflineMapper.countOfflineKeyNum();
        map.put("usedNum",usedNum);
        map.put("totalNum",totalNum + offlineKeyNum);
        return map;
    }

    @Override
    public String getAdminEmail() {
        String adminEmail = keyInfoMapper.getAdminEmail();
        return adminEmail;
    }
}
