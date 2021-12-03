package com.qtec.unicom.service.impl;

import com.qtec.unicom.controller.vo.KeySourceConfigRequest;
import com.qtec.unicom.mapper.KeySourceConfigMapper;
import com.qtec.unicom.pojo.KeySourceConfig;
import com.qtec.unicom.service.KeySourceConfigService;
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
}
