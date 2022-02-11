package com.unicom.quantum.service;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.pojo.App;

import java.util.List;

public interface AppService {
    int addApp(App app) throws QuantumException;
    int deleteApp(int appId);
    List<App> getApps();
}
