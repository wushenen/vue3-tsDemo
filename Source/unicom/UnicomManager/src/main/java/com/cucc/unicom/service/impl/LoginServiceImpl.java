package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.LoginMapper;
import com.cucc.unicom.pojo.User;
import com.cucc.unicom.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public User systemUserLogin(String userName) {
        return loginMapper.systemUserLogin(userName);
    }
}
