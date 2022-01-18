package com.cucc.unicom.service;

import com.cucc.unicom.controller.vo.UserAppRequest;
import com.cucc.unicom.pojo.dto.CurrentAppManager;
import com.cucc.unicom.pojo.dto.CurrentManagerApp;

import java.util.List;

public interface UserAppService {
    int addUserApp(UserAppRequest userAppRequest);
    int deleteUserApp(UserAppRequest userAppRequest);
    List<CurrentAppManager> getCurrentAppManager(int appId);
    List<CurrentManagerApp> getCurrentManagerApp(int userId);
}
