package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.util.DataTools;
import com.unicom.quantum.mapper.LoginMapper;
import com.unicom.quantum.pojo.DeviceUser;
import com.unicom.quantum.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public DeviceUser deviceUserLogin(String deviceName) throws Exception {
        DeviceUser deviceUser = loginMapper.deviceUserLogin(deviceName);
        deviceUser.setPassword(DataTools.decryptMessage(deviceUser.getPassword()));
        deviceUser.setEncKey(DataTools.decryptMessage(deviceUser.getEncKey()));
        return deviceUser;
    }
}
