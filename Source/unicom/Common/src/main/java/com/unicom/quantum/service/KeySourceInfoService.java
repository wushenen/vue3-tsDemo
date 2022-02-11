package com.unicom.quantum.service;

import com.unicom.quantum.pojo.dto.KeySourceDetail;

import java.util.List;

public interface KeySourceInfoService {
    int updateKeySourceInfo(int keySource, String key_generate_rate, Long key_generate_num);
    List<KeySourceDetail> getKeySourceInfo();
}
