package com.cucc.unicom.service;

import com.cucc.unicom.pojo.KeySourceConfig;
import com.cucc.unicom.controller.vo.KeySourceConfigRequest;

import java.util.List;

public interface KeySourceConfigService {

    int updateKeySourceConfig(KeySourceConfigRequest keySourceConfigRequest);
    int updateKeySourcePriority(int oldPriority,int newPriority,int id);
//    void updateKeySourcePriorityById(int priority,int id);
    List<KeySourceConfig> getKeySourceConfig();
    int enableKeySourceConfig(int priority,int id);
    int disableKeySourceConfig(int priority,int id);
    void updateQKDConfig(String configInfo);
    String getQKDConfig();

}
