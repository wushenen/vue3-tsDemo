package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.mapper.LoginMapper;
import com.unicom.quantum.pojo.App;
import com.unicom.quantum.service.LoginService;
import com.unicom.quantum.pojo.DeviceUser;
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
