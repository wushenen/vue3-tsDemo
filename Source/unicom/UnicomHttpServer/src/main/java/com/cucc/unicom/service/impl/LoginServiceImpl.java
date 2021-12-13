package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.LoginMapper;
import com.cucc.unicom.service.LoginService;
import com.cucc.unicom.pojo.DeviceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public DeviceUser deviceUserLogin(String deviceName) {
        return loginMapper.deviceUserLogin(deviceName);
    }
}
