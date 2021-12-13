package com.cucc.unicom.service;


import com.cucc.unicom.pojo.LockUser;

public interface LockUserService {

    int addLockUser(LockUser lockUser);

    int updateLockUser(LockUser lockUser);

    LockUser getLockUser(String userName);

}
