package com.unicom.quantum.service;

import com.unicom.quantum.pojo.KeyLimit;

public interface KeyLimitService {
    int updateKeyLimit(KeyLimit keyLimit);
    int deleteKeyLimit(KeyLimit keyLimit);
}
