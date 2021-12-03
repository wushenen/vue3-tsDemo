package com.qtec.unicom.mapper;


import com.qtec.unicom.pojo.AppSecret;
import com.qtec.unicom.pojo.AppUser;
import com.qtec.unicom.pojo.DeviceUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 系统用户|应用用户|终端用户 登录
 * */
@Mapper
@Repository
public interface LoginMapper {

    @Select("select * from t_device_user where binary device_name = #{deviceName}")
    DeviceUser deviceUserLogin(String deviceName);

}
