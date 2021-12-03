package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.RoleDeviceUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleDeviceUserMapper {

    /*添加角色已赋予终端用户*/
    int addRoleDeviceUser(@Param("deviceId") int deviceId, @Param("roleId") int roleId);

    /*判断角色终端用户是否已赋予*/
    boolean existAddRoleDeviceUser(@Param("deviceId") int deviceId, @Param("roleId") int roleId);

    /*取消角色赋予终端用户*/
    int deleteRoleDeviceUser(List<Integer> ids);

    /*获取角色赋予终端用户*/
    List<RoleDeviceUserInfo> roleDeviceUserList(@Param("roleId") int roleId);

}
