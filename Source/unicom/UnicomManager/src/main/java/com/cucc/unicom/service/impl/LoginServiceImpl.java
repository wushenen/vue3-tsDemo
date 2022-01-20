package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.mapper.LoginMapper;
import com.cucc.unicom.pojo.User;
import com.cucc.unicom.service.LoginService;
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
