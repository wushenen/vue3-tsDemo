package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.controller.vo.KeySourceConfigRequest;
import com.cucc.unicom.mapper.KeySourceConfigMapper;
import com.cucc.unicom.pojo.KeySourceConfig;
import com.cucc.unicom.pojo.dto.KeySourceDetail;
import com.cucc.unicom.service.KeySourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeySourceConfigServiceImpl implements KeySourceConfigService {

    private final String OPERATE_MODEL = "量子密钥源配置";

    @Autowired
    private KeySourceConfigMapper keySourceConfigMapper;

    @OperateLogAnno(operateDesc = "修改密钥源信息", operateModel = OPERATE_MODEL)
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

    @OperateLogAnno(operateDesc = "修改量子密钥源优先级", operateModel = OPERATE_MODEL)
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

    @OperateLogAnno(operateDesc = "查看量子密钥源配置", operateModel = OPERATE_MODEL)
    @Override
    public List<KeySourceDetail> getKeySourceConfig() {
        return keySourceConfigMapper.getKeySourceConfigs();
    }

    @OperateLogAnno(operateDesc = "启用量子密钥源配置", operateModel = OPERATE_MODEL)
    @Override
    public int enableKeySourceConfig(int priority, int id) {
        KeySourceConfig keySourceConfig = keySourceConfigMapper.getKeySourceConfig(id);
        if (keySourceConfig.getPriority() != 5)
            return 1;
        keySourceConfigMapper.enablePriority(priority);
        keySourceConfigMapper.updateKeySourcePriorityById(priority, id);
        return 0;
    }

    @OperateLogAnno(operateDesc = "禁用量子密钥源配置", operateModel = OPERATE_MODEL)
    @Override
    public int disableKeySourceConfig(int priority, int id) {
        List<KeySourceConfig> enableKeySourceConfigs = keySourceConfigMapper.getEnableKeySourceConfigs();
        if (enableKeySourceConfigs.size() == 1)
            return 1;
        keySourceConfigMapper.disablePriority(priority);
        keySourceConfigMapper.updateKeySourcePriorityById(5, id);
        return 0;
    }

    @OperateLogAnno(operateDesc = "修改量子密钥源QKD配置", operateModel = OPERATE_MODEL)
    @Override
    public void updateQKDConfig(String configInfo) {
        keySourceConfigMapper.updateQKDConfig(configInfo);
    }

    @Override
    public String getQKDConfig() {
        return keySourceConfigMapper.getQKDConfig();
    }
}
