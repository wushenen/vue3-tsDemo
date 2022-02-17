package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.DTO.AuthInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceAuthorityMapper {

    int addDeviceAuthority(@Param("deviceId") int deviceId, @Param("apiId") int apiId);
    boolean deviceAuthorityIsExist(@Param("deviceId") int deviceId, @Param("apiId") int apiId);
    int delDeviceAuthority(@Param("authId") int authId);
    int delDeviceAuthByDeviceId(@Param("deviceId") int deviceId);
    List<AuthInfo> getDeviceAuthority(@Param("deviceId") int deviceId);
    String getApiName(Integer apiId);
}
