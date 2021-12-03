package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.LockUserMapper;
import com.qtec.unicom.pojo.LockUser;
import com.qtec.unicom.service.LockUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockUserServiceImpl implements LockUserService {

    @Autowired
    private LockUserMapper lockUserMapper;

    @Override
    public int addLockUser(LockUser lockUser) {
        return lockUserMapper.addLockUser(lockUser);
    }

    @Override
    public int updateLockUser(LockUser lockUser) {
        return lockUserMapper.updateLockUser(lockUser);
    }

    @Override
    public LockUser getLockUser(String userName) {
        return lockUserMapper.getLockUser(userName);
    }
}
