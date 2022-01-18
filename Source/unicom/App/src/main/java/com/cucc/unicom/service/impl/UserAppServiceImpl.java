package com.cucc.unicom.service.impl;

import com.cucc.unicom.controller.vo.UserAppRequest;
import com.cucc.unicom.mapper.UserAppMapper;
import com.cucc.unicom.pojo.dto.CurrentAppManager;
import com.cucc.unicom.pojo.dto.CurrentManagerApp;
import com.cucc.unicom.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAppServiceImpl implements UserAppService {

    @Autowired
    private UserAppMapper userAppMapper;

    @Override
    public int addUserApp(UserAppRequest userAppRequest) {
        for (Integer userId : userAppRequest.getUserIds()) {
            if (!userAppMapper.userAppExist(userAppRequest.getAppId(),userId)) {
                userAppMapper.addUserApp(userAppRequest.getAppId(),userId);
            }
        }
        return 0;
    }

    @Override
    public int deleteUserApp(UserAppRequest userAppRequest) {
        for (Integer userId : userAppRequest.getUserIds()) {
            userAppMapper.deleteUserApp(userAppRequest.getAppId(),userId);
        }
        return 0;
    }

    @Override
    public List<CurrentAppManager> getCurrentAppManager(int appId) {
        return userAppMapper.getCurrentAppManager(appId);
    }

    @Override
    public List<CurrentManagerApp> getCurrentManagerApp(int userId) {
        return userAppMapper.getCurrentManagerApp(userId);
    }
}
