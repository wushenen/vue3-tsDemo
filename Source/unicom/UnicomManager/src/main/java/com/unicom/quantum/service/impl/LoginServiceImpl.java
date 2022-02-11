package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.LoginMapper;
import com.unicom.quantum.service.LoginService;
import com.unicom.quantum.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @OperateLogAnno(operateDesc = "系统用户登录", operateModel = "登录")
    @Override
    public User systemUserLogin(String userName) {
        return loginMapper.systemUserLogin(userName);
    }
}
