package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.controller.vo.AddUserRequest;
import com.qtec.unicom.controller.vo.UpdateUserRequest;
import com.qtec.unicom.mapper.UserMapper;
import com.qtec.unicom.pojo.User;
import com.qtec.unicom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final String OPERATE_MODEL = "系统用户模块";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UtilService utilService;

    @OperateLogAnno(operateDesc = "添加系统用户", operateModel = OPERATE_MODEL)
    @Override
    public int addUser(AddUserRequest addUserRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        if (userMapper.userExist(addUserRequest.getUserName())) {
            return 1;
        }
        User user = new User();
        user.setUserName(addUserRequest.getUserName())
        .setPassword(utilService.encryptCBC(addUserRequest.getPassword(),UtilService.SM4KEY))
        .setEmail(addUserRequest.getEmail())
        .setAccountType(addUserRequest.getAccountType())
        .setComments(addUserRequest.getComments());
        System.out.println(user.getPassword());
        System.out.println(user.getPassword().length());
        userMapper.addUser(user);
        return 0;
    }

    @Override
    public boolean userExist(String userName) {
        return userMapper.userExist(userName);
    }

    @OperateLogAnno(operateDesc = "修改系统用户信息", operateModel = OPERATE_MODEL)
    @Override
    public int updateUser(UpdateUserRequest updateUserRequest) {
        User user = new User();
        user.setId(updateUserRequest.getId());
        user.setPassword(utilService.encryptCBC(updateUserRequest.getPassword(),UtilService.SM4KEY));
        user.setComments(updateUserRequest.getComments());
        user.setEmail(updateUserRequest.getEmail());
        return userMapper.updateUser(user);
    }

    @OperateLogAnno(operateDesc = "删除系统用户", operateModel = OPERATE_MODEL)
    @Override
    public int deleteUser(int id) {
        return userMapper.deleteUser(id);
    }

    @OperateLogAnno(operateDesc = "获取系统用户信息", operateModel = OPERATE_MODEL)
    @Override
    public List<User> getUserInfos(String userName) {
        return userMapper.getUserInfos(userName);
    }

}
