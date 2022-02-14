package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.controller.vo.AddUserRequest;
import com.unicom.quantum.controller.vo.UpdateUserRequest;
import com.unicom.quantum.mapper.UserMapper;
import com.unicom.quantum.pojo.User;
import com.unicom.quantum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int addUser(AddUserRequest addUserRequest) throws QuantumException {
        if (userMapper.userExist(addUserRequest.getUserName())) {
            throw new QuantumException(ResultHelper.genResult(1,"用户名已被占用"));
        }
        User user = new User();
        user.setUserName(addUserRequest.getUserName())
            .setPassword(utilService.encryptCBCWithPadding(addUserRequest.getPassword(),UtilService.SM4KEY))
            .setEmail(addUserRequest.getEmail())
            .setAccountType(addUserRequest.getAccountType())
            .setComments(addUserRequest.getComments());
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
        if (!updateUserRequest.getPassword().equals("")){
            user.setPassword(utilService.encryptCBCWithPadding(updateUserRequest.getPassword(),UtilService.SM4KEY));
        }
        user.setComments(updateUserRequest.getComments());
        user.setEmail(updateUserRequest.getEmail());
        userMapper.updateUser(user);
        return 0;
    }

    @OperateLogAnno(operateDesc = "删除系统用户", operateModel = OPERATE_MODEL)
    @Override
    public int deleteUser(int id) {
        User user = userMapper.getUserInfoById(id);
        if (user.getAccountType() == 1)
            userMapper.deleteUserApp(id);
        userMapper.deleteUser(id);
        return 0;
    }

    @OperateLogAnno(operateDesc = "查看系统用户信息", operateModel = OPERATE_MODEL)
    @Override
    public List<User> getUserInfos(String userName) {
        return userMapper.getUserInfos(userName);
    }

    @OperateLogAnno(operateDesc = "查看应用管理员信息", operateModel = OPERATE_MODEL)
    @Override
    public List<User> getAppManager() {
        return userMapper.getAppManager();
    }
}
