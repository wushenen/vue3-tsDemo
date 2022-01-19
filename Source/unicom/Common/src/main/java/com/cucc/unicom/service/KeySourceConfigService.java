package com.cucc.unicom.service;

import com.cucc.unicom.controller.vo.KeySourceConfigRequest;
import com.cucc.unicom.pojo.dto.KeySourceDetail;

import java.util.List;

public interface KeySourceConfigService {

    int updateKeySourceConfig(KeySourceConfigRequest keySourceConfigRequest);
    int updateKeySourcePriority(int oldPriority, int newPriority, int id);
    List<KeySourceDetail> getKeySourceConfig();
    int enableKeySourceConfig(int priority, int id);
    int disableKeySourceConfig(int priority, int id);
    void updateQKDConfig(String configInfo);
    String getQKDConfig();

}
