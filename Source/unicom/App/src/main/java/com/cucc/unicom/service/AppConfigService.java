package com.cucc.unicom.service;

import com.cucc.unicom.controller.vo.UpdateAppConfigRequest;
import com.cucc.unicom.pojo.AppConfig;
import com.cucc.unicom.pojo.DeviceOperation;

public interface AppConfigService {

    AppConfig getAppConfig(int deviceId);

    AppConfig getAppConfigByAppId(int appId);

    int updateAppConfig(UpdateAppConfigRequest updateAppConfigRequest);

    int addQemsOperation(String deviceName, int operation);

    DeviceOperation getOperation(String deviceName);

    int delQemsOperation(String deviceName);


}
