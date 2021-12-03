package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.AuthInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceAuthorityMapper {

    /*添加终端权限*/
    int addDeviceAuthority(@Param("deviceId") int deviceId, @Param("apiId") int apiId);
    /*判断终端权限是否已被添加*/
    boolean deviceAuthorityIsAdd(@Param("deviceId") int deviceId, @Param("apiId") int apiId);

    /*删除终端权限（可批量）*/
    int delDeviceAuthority(@Param("authId") int authId);

    /*删除终端所有权限*/
    int delDeviceAuthByDeviceId(@Param("deviceId") int deviceId);

    /*获取指定终端已添加权限信息*/
    List<AuthInfo> getDeviceAuthority(@Param("deviceId") int deviceId);
}
