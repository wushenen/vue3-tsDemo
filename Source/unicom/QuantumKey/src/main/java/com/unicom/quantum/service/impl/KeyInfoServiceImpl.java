package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.KeyInfoMapper;
import com.unicom.quantum.mapper.KeyLimitMapper;
import com.unicom.quantum.pojo.KeyInfo;
import com.unicom.quantum.service.KeyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyInfoServiceImpl implements KeyInfoService {

    private final String OPERATE_MODEL = "量子密钥模块";

    @Autowired
    private KeyInfoMapper keyInfoMapper;

    @Autowired
    private KeyLimitMapper keyLimitMapper;

    @Override
    public void addKeyInfo(byte[] keyId,byte[] keyValue,String applicant, int keyStatus) {
        keyInfoMapper.addKeyInfo(keyId, keyValue, applicant,keyStatus);
    }

    @OperateLogAnno(operateDesc = "获取量子密钥", operateModel = OPERATE_MODEL)
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

    @OperateLogAnno(operateDesc = "销毁量子密钥", operateModel = OPERATE_MODEL)
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
