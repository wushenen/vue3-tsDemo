package com.unicom.quantum.mapper;


import com.unicom.quantum.pojo.App;
import com.unicom.quantum.pojo.DeviceUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 应用用户|终端用户 登录
 * */
@Mapper
@Repository
public interface LoginMapper {

    DeviceUser deviceUserLogin(String deviceName);

    App appLogin(String appKey);
}
