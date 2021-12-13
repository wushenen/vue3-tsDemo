package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.KeySourceConfigMapper;
import com.cucc.unicom.pojo.KeySourceConfig;
import com.cucc.unicom.service.KeySourceConfigService;
import com.cucc.unicom.controller.vo.KeySourceConfigRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeySourceConfigServiceImpl implements KeySourceConfigService {

    @Autowired
    private KeySourceConfigMapper keySourceConfigMapper;

    @Override
    public int updateKeySourceConfig(KeySourceConfigRequest keySourceConfigRequest) {
        KeySourceConfig keySourceConfig = keySourceConfigMapper.getKeySourceConfig(keySourceConfigRequest.getId());
        if (keySourceConfig.getPriority()==5) {
            return 2;
        }
        if (keySourceConfigRequest.getSourceIp2() != null && keySourceConfigRequest.getSourceIp()==null){
            return 1;
        }
        keySourceConfigMapper.updateKeySourceConfig(keySourceConfigRequest);
        return 0;
    }

    @Override
    public int updateKeySourcePriority(int oldPriority,int newPriority,int id) {
        if (newPriority > oldPriority) {
            keySourceConfigMapper.upwardPriority(oldPriority,newPriority);
        }else {
            keySourceConfigMapper.downwardPriority(oldPriority,newPriority);
        }
        keySourceConfigMapper.updateKeySourcePriorityById(newPriority, id);
        return 0;
    }

    @Override
    public List<KeySourceConfig> getKeySourceConfig() {
        return keySourceConfigMapper.getKeySourceConfigs();
    }


    @Override
    public int enableKeySourceConfig(int priority, int id) {
        KeySourceConfig keySourceConfig = keySourceConfigMapper.getKeySourceConfig(id);
        if (keySourceConfig.getPriority() != 5)
            return 1;
        keySourceConfigMapper.enablePriority(priority);
        keySourceConfigMapper.updateKeySourcePriorityById(priority, id);
        return 0;
    }

    @Override
    public int disableKeySourceConfig(int priority, int id) {
        List<KeySourceConfig> enableKeySourceConfigs = keySourceConfigMapper.getEnableKeySourceConfigs();
        if (enableKeySourceConfigs.size() == 1)
            return 1;
        keySourceConfigMapper.disablePriority(priority);
        keySourceConfigMapper.updateKeySourcePriorityById(5, id);
        return 0;
    }

    @Override
    public void updateQKDConfig(String configInfo) {
        keySourceConfigMapper.updateQKDConfig(configInfo);
    }

    @Override
    public String getQKDConfig() {
        return keySourceConfigMapper.getQKDConfig();
    }
}
