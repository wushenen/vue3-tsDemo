package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.DataTools;
import com.unicom.quantum.controller.vo.UserAppRequest;
import com.unicom.quantum.mapper.UserAppMapper;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.dto.CurrentAppManager;
import com.unicom.quantum.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAppServiceImpl implements UserAppService {

    private final String OPERATE_MODEL = "应用模块";

    @Autowired
    private UserAppMapper userAppMapper;

    @OperateLogAnno(operateDesc = "绑定应用管理员", operateModel = OPERATE_MODEL)
    @Override
    public int addUserApp(UserAppRequest userAppRequest) {
        for (Integer userId : userAppRequest.getUserIds()) {
            if (!userAppMapper.userAppExist(userAppRequest.getAppId(),userId)) {
                userAppMapper.addUserApp(userAppRequest.getAppId(),userId);
            }
        }
        return 0;
    }

    @OperateLogAnno(operateDesc = "取消绑定应用管理员", operateModel = OPERATE_MODEL)
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

    @OperateLogAnno(operateDesc = "查看应用管理员管理的全部应用", operateModel = OPERATE_MODEL)
    @Override
    public List<App> getCurrentManagerApp(int userId) throws Exception {
        List<App> currentManagerApp = userAppMapper.getCurrentManagerApp(userId);
        for (App app : currentManagerApp) {
            app.setAppKey(DataTools.decryptMessage(app.getAppKey()));
            app.setAppSecret(DataTools.decryptMessage(app.getAppSecret()));
        }
        return currentManagerApp;
    }
}
