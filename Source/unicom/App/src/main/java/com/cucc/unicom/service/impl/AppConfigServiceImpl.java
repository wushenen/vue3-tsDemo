package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.controller.vo.UpdateAppConfigRequest;
import com.cucc.unicom.mapper.AppConfigConfigMapper;
import com.cucc.unicom.pojo.AppConfig;
import com.cucc.unicom.pojo.DeviceOperation;
import com.cucc.unicom.service.AppConfigService;
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


    @OperateLogAnno(operateDesc = "修改QEMS配置", operateModel = OPERATE_MODEL)
    @Override
    public int updateAppConfig(UpdateAppConfigRequest updateAppConfigRequest) {
        //如果没有改动则不用更新时间，后续可以作为优化点
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

    @OperateLogAnno(operateDesc = "设备指令控制", operateModel = OPERATE_MODEL)
    @Override
    public int addQemsOperation(String deviceName, int operation) {
        if (appConfigConfigMapper.countQemsOperation(deviceName) != 0)
            return 1;
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
