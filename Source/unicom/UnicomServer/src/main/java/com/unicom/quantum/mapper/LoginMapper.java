package com.unicom.quantum.mapper;


import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.DeviceUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 应用用户|终端用户 登录
 * */
@Mapper
@Repository
public interface LoginMapper {

    @Select("select * from t_device_user where binary device_name = #{deviceName}")
    DeviceUser deviceUserLogin(String deviceName);

    @Select("select * from t_app where binary app_key = #{appKey}")
    App appLogin(String appKey);
}
