package com.cucc.unicom.service;

import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.dto.AppBaseInfo;

import java.util.List;

public interface AppService {
    int addApp(App app);
    int deleteApp(int appId);
    List<App> getApps();
    List<AppBaseInfo> getAppBaseInfo();
}
