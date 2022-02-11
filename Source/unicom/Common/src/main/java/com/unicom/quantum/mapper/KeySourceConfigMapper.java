package com.unicom.quantum.mapper;

import com.unicom.quantum.controller.vo.KeySourceConfigRequest;
import com.unicom.quantum.pojo.KeySourceConfig;
import com.unicom.quantum.pojo.dto.KeySourceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KeySourceConfigMapper {
    void upwardPriority(int oldPriority, int newPriority);
    void downwardPriority(int oldPriority, int newPriority);
    void enablePriority(int priority);
    void disablePriority(int priority);
    List<KeySourceDetail> getKeySourceConfigs();
    List<KeySourceConfig> getEnableKeySourceConfigs();
    KeySourceConfig getKeySourceConfig(int id);
    void updateKeySourceConfig(KeySourceConfigRequest keySourceConfigRequest);
    void updateKeySourcePriorityById(int priority, int id);
    void updateQKDConfig(String configInfo);
    String getQKDConfig();
}
