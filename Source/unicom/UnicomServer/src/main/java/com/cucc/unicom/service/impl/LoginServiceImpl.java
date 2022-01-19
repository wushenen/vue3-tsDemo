package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.util.UtilService;
import com.cucc.unicom.mapper.LoginMapper;
import com.cucc.unicom.pojo.App;
import com.cucc.unicom.service.LoginService;
import com.cucc.unicom.pojo.DeviceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private UtilService utilService;

    @Override
    public DeviceUser deviceUserLogin(String deviceName) {
        return loginMapper.deviceUserLogin(deviceName);
    }

    @Override
    public App appLogin(String appKey) {
        return loginMapper.appLogin(utilService.decryptCBCWithPadding(appKey, UtilService.SM4KEY));
    }
}
