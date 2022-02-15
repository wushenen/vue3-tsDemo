package com.unicom.quantum.service;

import com.unicom.quantum.pojo.DeviceUser;

public interface LoginService {
    DeviceUser deviceUserLogin(String deviceName) throws Exception;
}
