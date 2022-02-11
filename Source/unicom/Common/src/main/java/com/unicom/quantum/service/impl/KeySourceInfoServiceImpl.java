package com.unicom.quantum.service.impl;

import com.unicom.quantum.mapper.KeySourceInfoMapper;
import com.unicom.quantum.service.KeySourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class KeySourceInfoServiceImpl implements KeySourceInfoService {

    @Autowired
    private KeySourceInfoMapper keySourceInfoMapper;

    @Override
    public int updateKeySourceInfo(int keySource, String keyGenerateRate, Long keyGenerateNum) {
        if (keySourceInfoMapper.keySourceInfoExist(keySource)) {
           keySourceInfoMapper.updateKeySourceInfo(keySource, keyGenerateRate, keyGenerateNum);
        }else {
            keySourceInfoMapper.addKeySourceInfo(keySource, keyGenerateRate, keyGenerateNum);
        }
        return 0;
    }
}
