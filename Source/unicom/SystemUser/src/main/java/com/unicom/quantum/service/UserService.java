package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.UpdateUserRequest;
import com.unicom.quantum.pojo.User;
import com.unicom.quantum.controller.vo.AddUserRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

public interface UserService {

    int addUser(AddUserRequest addUserRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException;

    boolean userExist(String userName);

    int updateUser(UpdateUserRequest updateUserRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException;

    int deleteUser(int id);

    List<User> getUserInfos(String userName);

    List<User> getAppManager();

}
