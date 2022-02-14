package com.unicom.quantum.mapper;


import com.unicom.quantum.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginMapper {

    User systemUserLogin(String userName);
}
