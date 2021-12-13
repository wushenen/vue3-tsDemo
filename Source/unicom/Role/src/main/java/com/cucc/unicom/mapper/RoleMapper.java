package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {

    /*创建角色*/
    int addRole(@Param("roleCode") String roleCode, @Param("roleDescribe") String roleDescribe);

    /*判断角色代码唯一性*/
    int uniqueRole(@Param("roleCode") String roleCode);

    /**
     * 查看角色信息
     * */
    /*获取角色信息列表*/
    List<Role> roleList();

    /*模糊查询角色信息（授权获取角色信息时使用）*/
    List<Role> searchRole(@Param("roleCode") String roleCode);

    /*查看角色详情*/
    Role roleInfo(@Param("roleCode") String roleCode);

    /*根据role_id查询信息*/
    Role roleInfoById(@Param("roleId") int roleId);

    /*删除角色*/
    int delRole(@Param("roleId") int roleId);
    /*同时删除该角色下的所有用户*/
    int delRoleUser(@Param("roleId") int roleId);
    /*同时删除策略授权角色信息*/
    int delStrategyAuthByRoleId(int roleId);

    /*编辑角色信息*/
    int updateRoleInfo(@Param("roleId") int roleId, @Param("roleCode") String roleCode, @Param("roleDescribe") String roleDescribe);

}
