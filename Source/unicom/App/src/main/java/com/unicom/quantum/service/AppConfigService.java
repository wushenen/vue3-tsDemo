package com.unicom.quantum.service;

import com.unicom.quantum.pojo.AppConfig;
import com.unicom.quantum.controller.vo.UpdateAppConfigRequest;
import com.unicom.quantum.pojo.DeviceOperation;

public interface AppConfigService {

    AppConfig getAppConfig(int deviceId);

    AppConfig getAppConfigByAppId(int appId);

    int updateAppConfig(UpdateAppConfigRequest updateAppConfigRequest);

    int addQemsOperation(String deviceName, int operation);

    DeviceOperation getOperation(String deviceName);

    int delQemsOperation(String deviceName);


}
