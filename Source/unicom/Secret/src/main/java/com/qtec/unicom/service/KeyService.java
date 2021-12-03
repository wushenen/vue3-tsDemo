package com.qtec.unicom.service;

import com.qtec.unicom.pojo.Key;

public interface KeyService {

    Key getKeySpecAndIndex(String keyId) throws Exception;

}
