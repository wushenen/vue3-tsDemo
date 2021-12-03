package com.qtec.unicom.service;

import com.qtec.unicom.controller.vo.KeySourceConfigRequest;
import com.qtec.unicom.pojo.KeySourceConfig;

import java.util.List;

public interface KeySourceConfigService {

    int updateKeySourceConfig(KeySourceConfigRequest keySourceConfigRequest);
    int updateKeySourcePriority(int oldPriority,int newPriority,int id);
//    void updateKeySourcePriorityById(int priority,int id);
    List<KeySourceConfig> getKeySourceConfig();
    int enableKeySourceConfig(int priority,int id);
    int disableKeySourceConfig(int priority,int id);


}
