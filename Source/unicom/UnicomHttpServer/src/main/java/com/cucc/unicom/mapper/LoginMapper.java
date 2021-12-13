package com.cucc.unicom.mapper;


import com.cucc.unicom.pojo.DeviceUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 终端用户 登录
 * */
@Mapper
@Repository
public interface LoginMapper {

    @Select("select * from t_device_user where binary device_name = #{deviceName}")
    DeviceUser deviceUserLogin(String deviceName);

}
