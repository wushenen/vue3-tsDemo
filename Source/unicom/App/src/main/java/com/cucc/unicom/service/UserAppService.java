package com.cucc.unicom.service;

import com.cucc.unicom.controller.vo.UserAppRequest;
import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.dto.CurrentAppManager;

import java.util.List;

public interface UserAppService {
    int addUserApp(UserAppRequest userAppRequest);
    int deleteUserApp(UserAppRequest userAppRequest);
    List<CurrentAppManager> getCurrentAppManager(int appId);
    List<App> getCurrentManagerApp(int userId);
}
