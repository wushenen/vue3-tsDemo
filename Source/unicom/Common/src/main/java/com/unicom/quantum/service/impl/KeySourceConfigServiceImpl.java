package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.controller.vo.KeySourceConfigRequest;
import com.unicom.quantum.mapper.KeySourceConfigMapper;
import com.unicom.quantum.service.KeySourceConfigService;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.pojo.KeySourceConfig;
import com.unicom.quantum.pojo.dto.KeySourceDetail;
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
    public int updateKeySourceConfig(KeySourceConfigRequest keySourceConfigRequest) throws QuantumException {
        KeySourceConfig keySourceConfig = keySourceConfigMapper.getKeySourceConfig(keySourceConfigRequest.getId());
        if (keySourceConfig.getPriority()==4)
            throw new QuantumException(ResultHelper.genResult(1,"请启用该量子密钥后再设置IP"));
        if (keySourceConfigRequest.getSourceIp2() != null && keySourceConfigRequest.getSourceIp()==null)
            throw new QuantumException(ResultHelper.genResult(1,"主IP未设置时禁止设置备用IP"));
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
    public int enableKeySourceConfig(int priority, int id) throws QuantumException {
        KeySourceConfig keySourceConfig = keySourceConfigMapper.getKeySourceConfig(id);
        if (keySourceConfig.getPriority() != 4)
            throw new QuantumException(ResultHelper.genResult(1,"密钥源已启用，无需重复启用"));
        keySourceConfigMapper.enablePriority(priority);
        keySourceConfigMapper.updateKeySourcePriorityById(priority, id);
        return 0;
    }

    @OperateLogAnno(operateDesc = "禁用量子密钥源配置", operateModel = OPERATE_MODEL)
    @Override
    public int disableKeySourceConfig(int priority, int id) throws QuantumException {
        List<KeySourceConfig> enableKeySourceConfigs = keySourceConfigMapper.getEnableKeySourceConfigs();
        if (enableKeySourceConfigs.size() == 1)
            throw new QuantumException(ResultHelper.genResult(1,"禁止禁用所有量子密钥源"));
        keySourceConfigMapper.disablePriority(priority);
        keySourceConfigMapper.updateKeySourcePriorityById(4, id);
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
