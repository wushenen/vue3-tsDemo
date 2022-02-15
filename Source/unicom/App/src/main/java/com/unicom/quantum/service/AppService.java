package com.unicom.quantum.service;

import com.unicom.quantum.pojo.App;

import java.util.List;

public interface AppService {
    int addApp(App app) throws Exception;
    int deleteApp(int appId);
    List<App> getApps() throws Exception;
}
