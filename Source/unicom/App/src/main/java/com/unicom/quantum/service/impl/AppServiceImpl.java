package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.mapper.AppConfigConfigMapper;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.mapper.AppDeviceMapper;
import com.unicom.quantum.mapper.AppMapper;
import com.unicom.quantum.mapper.UserAppMapper;
import com.unicom.quantum.pojo.dto.AppDeviceDTO;
import com.unicom.quantum.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    private final String OPERATE_MODEL = "应用模块";

    @Autowired
    private AppMapper appMapper;
    @Autowired
    private UserAppMapper userAppMapper;
    @Autowired
    private AppDeviceMapper appDeviceMapper;
    @Autowired
    private AppConfigConfigMapper appConfigConfigMapper;
    @Autowired
    private UtilService utilService;

    @OperateLogAnno(operateDesc = "创建新应用", operateModel = OPERATE_MODEL)
    @Override
    public int addApp(App app) throws QuantumException {
        if (appMapper.appExist(app.getAppName())) {
            throw new QuantumException(ResultHelper.genResult(1,"应用名称已存在"));
        } else {
            app.setAppKey(utilService.encryptCBCWithPadding(utilService.createRandomCharData(24), UtilService.SM4KEY));
            app.setAppSecret(utilService.encryptCBCWithPadding(utilService.createRandomCharData(32), UtilService.SM4KEY));
            appMapper.addApp(app);
            return 0;
        }
    }

    @OperateLogAnno(operateDesc = "删除应用", operateModel = OPERATE_MODEL)
    @Override
    public int deleteApp(int appId) {
        appMapper.deleteApp(appId);
        appConfigConfigMapper.delAppConfig(appId);
        userAppMapper.deleteUserByAppId(appId);
        List<AppDeviceDTO> appDevice = appDeviceMapper.getAppDevice(appId);
        if (appDevice.size() != 0){
            ArrayList<String> deviceNames = new ArrayList<>();
            for (AppDeviceDTO appDeviceDTO : appDevice) {
                deviceNames.add(appDeviceDTO.getDeviceName());
            }
            appDeviceMapper.deleteDeviceStatusInfo(deviceNames);
        }
        return 0;
    }

    @OperateLogAnno(operateDesc = "获取应用信息", operateModel = OPERATE_MODEL)
    @Override
    public List<App> getApps() {
        List<App> list = appMapper.getApps();
        for (App app : list) {
            app.setAppKey(utilService.decryptCBCWithPadding(app.getAppKey(), UtilService.SM4KEY));
            app.setAppSecret(utilService.decryptCBCWithPadding(app.getAppSecret(), UtilService.SM4KEY));
        }
        return list;
    }

}
