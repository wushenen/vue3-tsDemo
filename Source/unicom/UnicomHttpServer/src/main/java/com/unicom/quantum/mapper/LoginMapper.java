package com.unicom.quantum.mapper;


import com.unicom.quantum.pojo.DeviceUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginMapper {

    DeviceUser deviceUserLogin(String deviceName);

}
