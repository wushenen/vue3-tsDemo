package com.unicom.quantum.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestMapper {

    @Update("update t_device_user set enc_key=#{encKey} where device_name = #{deviceName}")
    int updateEnc(byte[] encKey, String deviceName);

}
