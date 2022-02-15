package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.util.DataTools;
import com.unicom.quantum.mapper.LoginMapper;
import com.unicom.quantum.pojo.App;
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

    @Override
    public App appLogin(String appKey) throws Exception {
        App app = loginMapper.appLogin(DataTools.encryptMessage(appKey));
        if (app == null) {
            throw new QuantumException(ResultHelper.genResult(1,"应用不存在"));
        }else {
            app.setAppKey(DataTools.decryptMessage(app.getAppKey()));
            app.setAppSecret(DataTools.decryptMessage(app.getAppSecret()));
            return app;
        }
    }
}
