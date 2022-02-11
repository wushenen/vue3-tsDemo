package com.unicom.quantum.service.impl;

import com.unicom.quantum.mapper.ShiroAuthMapper;
import com.unicom.quantum.service.ShiroAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiroAuthServiceImpl implements ShiroAuthService {

    @Autowired
    private ShiroAuthMapper shiroAuthMapper;

    @Override
    public List<Integer> getGroupInfosByDeviceId(int deviceId) {
        return shiroAuthMapper.getGroupInfosByDeviceId(deviceId);
    }


}
