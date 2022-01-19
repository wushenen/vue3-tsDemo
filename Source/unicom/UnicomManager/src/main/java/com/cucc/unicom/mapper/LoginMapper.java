package com.cucc.unicom.mapper;


import com.cucc.unicom.pojo.AppSecret;
import com.cucc.unicom.pojo.AppUser;
import com.cucc.unicom.pojo.DeviceUser;
import com.cucc.unicom.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 系统用户|应用用户|终端用户 登录
 * */
@Mapper
@Repository
public interface LoginMapper {

    @Select("select * from t_user where binary user_name = #{userName}")
    User systemUserLogin(String userName);

    @Select("select * from t_app where binary app_key = #{appKey}")
    AppSecret appUserLogin(String appKey);

    @Select("select * from t_app_user where binary user_name = #{appUserName}")
    AppUser verifyAppUser(String appUserName);

    @Select("select * from t_device_user where binary device_name = #{deviceName}")
    DeviceUser deviceUserLogin(String deviceName);

}
