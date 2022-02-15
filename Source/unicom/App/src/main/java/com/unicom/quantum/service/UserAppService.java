package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.UserAppRequest;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.dto.CurrentAppManager;

import java.util.List;

public interface UserAppService {
    int addUserApp(UserAppRequest userAppRequest);
    int deleteUserApp(UserAppRequest userAppRequest);
    List<CurrentAppManager> getCurrentAppManager(int appId);
    List<App> getCurrentManagerApp(int userId) throws Exception;
}
