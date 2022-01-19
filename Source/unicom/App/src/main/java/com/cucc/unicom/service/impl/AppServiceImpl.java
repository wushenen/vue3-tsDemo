package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.util.UtilService;
import com.cucc.unicom.mapper.AppMapper;
import com.cucc.unicom.pojo.App;
import com.cucc.unicom.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;
    @Autowired
    private UtilService utilService;


    @Override
    public int addApp(App app) {
        if (appMapper.appExist(app.getAppName())) {
            return 1;
        } else {
            app.setAppKey(utilService.encryptCBCWithPadding(utilService.createRandomCharData(24), UtilService.SM4KEY));
            app.setAppSecret(utilService.encryptCBCWithPadding(utilService.createRandomCharData(32), UtilService.SM4KEY));
            appMapper.addApp(app);
            return 0;
        }
    }

    @Override
    public int deleteApp(int appId) {
        appMapper.deleteApp(appId);
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
