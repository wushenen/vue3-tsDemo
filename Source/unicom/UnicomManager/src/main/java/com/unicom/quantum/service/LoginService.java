package com.unicom.quantum.service;

import com.unicom.quantum.pojo.User;

public interface LoginService {
    User systemUserLogin(String userName) throws Exception;
}
