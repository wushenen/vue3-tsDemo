package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DeviceUser;

public interface LoginService {
    DeviceUser deviceUserLogin(String deviceName);
}
