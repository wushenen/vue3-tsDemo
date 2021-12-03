package com.qtec.unicom.service;

import com.qtec.unicom.pojo.AppSecret;
import com.qtec.unicom.pojo.AppUser;
import com.qtec.unicom.pojo.DeviceUser;
import com.qtec.unicom.pojo.User;

public interface LoginService {

    User systemUserLogin(String userName);
    AppSecret appUserLogin(String appKey, String appSecret);
    AppUser verifyAppUser(String appUserName);
    DeviceUser deviceUserLogin(String deviceName);


}
