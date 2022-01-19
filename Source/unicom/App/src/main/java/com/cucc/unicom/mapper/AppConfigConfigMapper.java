package com.cucc.unicom.mapper;

import com.cucc.unicom.controller.vo.UpdateAppConfigRequest;
import com.cucc.unicom.pojo.AppConfig;
import com.cucc.unicom.pojo.DeviceOperation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface AppConfigConfigMapper {

    int addAppConfig(AppConfig appConfig);

    int delAppConfig(int appId);

    boolean appConfigExist(int appId);

    /*终端获取配置信息*/
    AppConfig getAppConfig(int deviceId);

    /*应用获取配置信息*/
    AppConfig getAppConfigByAppId(int appId);

    int updateAppConfig(UpdateAppConfigRequest updateAppConfigRequest);

    //控制设备重启或置零
    int addQemsOperation(String deviceName, int operation);

    int countQemsOperation(String deviceName);

    DeviceOperation getOperation(String deviceName);

    int delQemsOperation(String deviceName);

}
