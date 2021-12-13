package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    int addUser(User user);

    boolean userExist(String userName);

    int updateUser(User user);

    int deleteUser(int id);

    List<User> getUserInfos(String userName);

}
