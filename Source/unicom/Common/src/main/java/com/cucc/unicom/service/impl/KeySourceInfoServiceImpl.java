package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.KeySourceInfoMapper;
import com.cucc.unicom.pojo.dto.KeySourceDetail;
import com.cucc.unicom.service.KeySourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<KeySourceDetail> getKeySourceInfo() {
        return null;
    }
}
