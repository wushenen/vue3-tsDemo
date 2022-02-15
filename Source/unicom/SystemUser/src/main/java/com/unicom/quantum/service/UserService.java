package com.unicom.quantum.service;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.controller.vo.AddUserRequest;
import com.unicom.quantum.controller.vo.UpdateUserRequest;
import com.unicom.quantum.pojo.User;

import java.util.List;

public interface UserService {

    int addUser(AddUserRequest addUserRequest) throws QuantumException;

    boolean userExist(String userName);

    int updateUser(UpdateUserRequest updateUserRequest);

    int deleteUser(int id);

    List<User> getUserInfos(String userName);

    List<User> getAppManager();

}
