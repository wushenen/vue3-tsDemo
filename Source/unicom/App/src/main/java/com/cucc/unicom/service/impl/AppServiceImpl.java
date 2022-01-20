package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.util.UtilService;
import com.cucc.unicom.mapper.AppConfigConfigMapper;
import com.cucc.unicom.mapper.AppDeviceMapper;
import com.cucc.unicom.mapper.AppMapper;
import com.cucc.unicom.mapper.UserAppMapper;
import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.AppConfig;
import com.cucc.unicom.pojo.dto.AppDeviceDTO;
import com.cucc.unicom.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppServiceImpl implements AppService {

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


    @Override
    public int addApp(App app) {
        if (appMapper.appExist(app.getAppName())) {
            return 1;
        } else {
            app.setAppKey(utilService.encryptCBCWithPadding(utilService.createRandomCharData(24), UtilService.SM4KEY));
            app.setAppSecret(utilService.encryptCBCWithPadding(utilService.createRandomCharData(32), UtilService.SM4KEY));
            if (app.getAppType()==2) {
                AppConfig appConfig = new AppConfig();
                appConfig.setAppId(app.getAppId());
                appConfigConfigMapper.addAppConfig(appConfig);
            }
            appMapper.addApp(app);
            return 0;
        }
    }

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
