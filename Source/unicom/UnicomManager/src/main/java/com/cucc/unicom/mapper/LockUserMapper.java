package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.LockUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LockUserMapper {

    int addLockUser(LockUser lockUser);

    int updateLockUser(LockUser lockUser);

    LockUser getLockUser(String userName);

}
