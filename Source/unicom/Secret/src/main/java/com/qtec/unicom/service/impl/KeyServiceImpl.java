package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.KeyMapper;
import com.qtec.unicom.pojo.Key;
import com.qtec.unicom.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyServiceImpl implements KeyService {

    @Autowired
    KeyMapper keyMapper;

    /**
     * 通过keyId获取主密钥的相关信息
     * @param keyId
     * @return
     */
    @Override
    public Key getKeySpecAndIndex(String keyId) {
        Key key = keyMapper.getKey(keyId);
        return key;
    }
}
