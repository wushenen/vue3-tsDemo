package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.LoginMapper;
import com.qtec.unicom.pojo.AppSecret;
import com.qtec.unicom.pojo.AppUser;
import com.qtec.unicom.pojo.DeviceUser;
import com.qtec.unicom.pojo.User;
import com.qtec.unicom.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public User systemUserLogin(String userName) {
        return loginMapper.systemUserLogin(userName);
    }

    @Override
    public AppSecret appUserLogin(String appKey, String appSecret) {
        AppSecret appUserInfo = loginMapper.appUserLogin(appKey);
        if (appUserInfo != null && appSecret.equals(appUserInfo.getAppSecret()))
            return appUserInfo;
        return null;
    }

    @Override
    public AppUser verifyAppUser(String appUserName) {
        return loginMapper.verifyAppUser(appUserName);
    }

    @Override
    public DeviceUser deviceUserLogin(String deviceName) {
        return loginMapper.deviceUserLogin(deviceName);
    }
}
