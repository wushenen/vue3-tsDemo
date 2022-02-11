package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.controller.vo.UpdateAppConfigRequest;
import com.unicom.quantum.mapper.AppConfigConfigMapper;
import com.unicom.quantum.pojo.AppConfig;
import com.unicom.quantum.pojo.DeviceOperation;
import com.unicom.quantum.service.AppConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppConfigServiceImpl implements AppConfigService {

    private final String OPERATE_MODEL = "应用配置模块";

    @Autowired
    private AppConfigConfigMapper appConfigConfigMapper;

    @Override
    public AppConfig getAppConfig(int deviceId) {
        return appConfigConfigMapper.getAppConfig(deviceId);
    }

    @Override
    public AppConfig getAppConfigByAppId(int appId) {
        return appConfigConfigMapper.getAppConfigByAppId(appId);
    }


    @OperateLogAnno(operateDesc = "修改应用配置", operateModel = OPERATE_MODEL)
    @Override
    public int updateAppConfig(UpdateAppConfigRequest updateAppConfigRequest) {
        if (appConfigConfigMapper.appConfigExist(updateAppConfigRequest.getAppId())) {
            appConfigConfigMapper.updateAppConfig(updateAppConfigRequest);
        }else {
            AppConfig appConfig = new AppConfig();
            BeanUtils.copyProperties(updateAppConfigRequest,appConfig);
            appConfig.setVersion(0);
            appConfigConfigMapper.addAppConfig(appConfig);
        }

        return 0;
    }

    @OperateLogAnno(operateDesc = "控制设备重启或置零", operateModel = OPERATE_MODEL)
    @Override
    public int addQemsOperation(String deviceName, int operation) throws QuantumException {
        if (appConfigConfigMapper.countQemsOperation(deviceName) != 0)
            throw new QuantumException(ResultHelper.genResult(1,"命令已下发，请耐心等待"));
        appConfigConfigMapper.addQemsOperation(deviceName, operation);
        return 0;
    }

    @Override
    public DeviceOperation getOperation(String deviceName) {
        return appConfigConfigMapper.getOperation(deviceName);
    }

    @Override
    public int delQemsOperation(String deviceName) {
        return appConfigConfigMapper.delQemsOperation(deviceName);
    }
}
