package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.User;
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

    int deleteUserApp(int id);

    List<User> getUserInfos(String userName);

    List<User> getAppManager();

    User getUserInfoById(int id);

}
