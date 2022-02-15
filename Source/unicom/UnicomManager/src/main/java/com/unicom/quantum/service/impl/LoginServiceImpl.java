package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.mapper.LoginMapper;
import com.unicom.quantum.pojo.User;
import com.unicom.quantum.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private UtilService utilService;

    @OperateLogAnno(operateDesc = "系统用户登录", operateModel = "登录")
    @Override
    public User systemUserLogin(String userName) throws Exception {
        User user = loginMapper.systemUserLogin(userName);
        if (user == null) {
            throw new QuantumException(ResultHelper.genResult(1,"用户不存在"));
        }else {
            user.setPassword(utilService.decryptCBCWithPadding(user.getPassword(),UtilService.SM4KEY));
            return user;
        }
    }
}
