package com.unicom.quantum.service.impl;

import com.unicom.quantum.mapper.LoginMapper;
import com.unicom.quantum.service.LoginService;
import com.unicom.quantum.pojo.DeviceUser;
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
