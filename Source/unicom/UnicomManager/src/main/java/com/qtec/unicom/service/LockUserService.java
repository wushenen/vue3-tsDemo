package com.qtec.unicom.service;


import com.qtec.unicom.pojo.LockUser;

public interface LockUserService {

    int addLockUser(LockUser lockUser);

    int updateLockUser(LockUser lockUser);

    LockUser getLockUser(String userName);

}
