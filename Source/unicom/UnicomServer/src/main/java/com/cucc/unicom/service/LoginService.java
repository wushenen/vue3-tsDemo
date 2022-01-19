package com.cucc.unicom.service;

import com.cucc.unicom.pojo.App;
import com.cucc.unicom.pojo.DeviceUser;

public interface LoginService {
    DeviceUser deviceUserLogin(String deviceName);
    App appLogin(String appKey);
}
