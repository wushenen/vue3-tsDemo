package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.LoginMapper;
import com.qtec.unicom.pojo.DeviceUser;
import com.qtec.unicom.service.LoginService;
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
