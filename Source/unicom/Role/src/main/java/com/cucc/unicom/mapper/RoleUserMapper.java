/*
package com.qtec.mixedquantum.mapper;

import com.qtec.mixedquantum.pojo.DTO.RoleUserDTO;
import com.qtec.mixedquantum.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleUserMapper {

    */
/*添加角色授权用户*//*

    int addRoleUser(@Param("deviceId") int deviceId, @Param("roleId") int roleId);

    */
/*判断角色用户是否已添加*//*

    int alreadyAdd(@Param("deviceId") int deviceId, @Param("roleId") int roleId);

    */
/*取消角色授权用户*//*

    int deleteRoleUser(@Param("deviceId") int deviceId, @Param("roleId") int roleId);

    */
/*获取角色已授权用户*//*

    List<RoleUserDTO> roleUserList(@Param("roleId") int roleId);


    */
/*根据deviceId查询角色code*,管理界面用不到*//*

    List<String> getRoleCode(@Param("deviceId") int deviceId);
}
*/
