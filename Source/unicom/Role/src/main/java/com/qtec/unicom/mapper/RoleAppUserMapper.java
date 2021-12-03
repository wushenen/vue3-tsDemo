package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.RoleAppUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleAppUserMapper {

    /*添加角色已赋予终端用户*/
    int addRoleAppUser(@Param("appUserId") int appUserId, @Param("roleId") int roleId);

    /*判断角色终端用户是否已赋予*/
    boolean existAddRoleAppUser(@Param("appUserId") int appUserId, @Param("roleId") int roleId);

    /*取消角色赋予终端用户*/
    int deleteRoleAppUser(List<Integer> ids);

    /*获取角色赋予终端用户*/
    List<RoleAppUserInfo> roleAppUserList(@Param("roleId") int roleId);

}
