package com.cucc.unicom.service;

import com.cucc.unicom.pojo.AppSecret;
import com.cucc.unicom.pojo.AppUser;
import com.cucc.unicom.pojo.DeviceUser;
import com.cucc.unicom.pojo.User;

public interface LoginService {

    User systemUserLogin(String userName);
    AppSecret appUserLogin(String appKey, String appSecret);
    AppUser verifyAppUser(String appUserName);
    DeviceUser deviceUserLogin(String deviceName);


}
