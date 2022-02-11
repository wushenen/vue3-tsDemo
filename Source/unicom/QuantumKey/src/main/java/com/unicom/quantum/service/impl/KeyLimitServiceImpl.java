package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.KeyLimitMapper;
import com.unicom.quantum.pojo.KeyLimit;
import com.unicom.quantum.service.KeyLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyLimitServiceImpl implements KeyLimitService {

    private final String OPERATE_MODEL = "量子密钥模块";

    @Autowired
    private KeyLimitMapper keyLimitMapper;

    @OperateLogAnno(operateDesc = "重新分配量子密钥额度", operateModel = OPERATE_MODEL)
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
