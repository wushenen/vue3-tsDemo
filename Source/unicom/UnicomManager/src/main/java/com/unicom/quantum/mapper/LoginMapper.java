package com.unicom.quantum.mapper;


import com.unicom.quantum.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 系统用户登录
 * */
@Mapper
@Repository
public interface LoginMapper {
    @Select("select * from t_user where binary user_name = #{userName}")
    User systemUserLogin(String userName);
}
