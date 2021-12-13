package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.KeySourceConfig;
import com.cucc.unicom.controller.vo.KeySourceConfigRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KeySourceConfigMapper {

    void upwardPriority(int oldPriority,int newPriority);
    void downwardPriority(int oldPriority,int newPriority);
    void enablePriority(int priority);
    void disablePriority(int priority);
//    int updateKeySourcePriority(int oldPriority,int newPriority);
    List<KeySourceConfig> getKeySourceConfigs();
    List<KeySourceConfig> getEnableKeySourceConfigs();
    KeySourceConfig getKeySourceConfig(int id);
    void updateKeySourceConfig(KeySourceConfigRequest keySourceConfigRequest);
    void updateKeySourcePriorityById(int priority,int id);

    void updateQKDConfig(String configInfo);
    String getQKDConfig();


    /*int getKeySourceConfigId(int priority);*/

}
