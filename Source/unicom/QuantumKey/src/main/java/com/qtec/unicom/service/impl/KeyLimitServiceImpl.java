package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.KeyLimitMapper;
import com.qtec.unicom.pojo.KeyLimit;
import com.qtec.unicom.service.KeyLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyLimitServiceImpl implements KeyLimitService {

    @Autowired
    private KeyLimitMapper keyLimitMapper;


    @Override
    public int updateKeyLimit(KeyLimit keyLimit) {
        if (keyLimitMapper.existKeyLimit(keyLimit)) {
            keyLimitMapper.updateKeyLimit(keyLimit);
        }else{
            keyLimitMapper.addKeyLimit(keyLimit);
        }
        return 0;
    }

    @Override
    public int deleteKeyLimit(KeyLimit keyLimit) {
        keyLimitMapper.deleteKeyLimit(keyLimit);
        return 0;
    }
}
