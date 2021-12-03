package com.qtec.unicom.service;

import com.qtec.unicom.pojo.Role;

import java.util.List;

public interface RoleService {

    /*创建角色*/
    int addRole(String roleCode, String roleDescribe);

    /*判断角色代码唯一性*/
    int uniqueRole(String roleCode);

    /**
     * 查看角色信息
     * */
    /*获取角色信息列表*/
    List<Role> roleList();

    /*获取角色信息（授权获取角色信息时使用）*/
    List<Role> searchRole(String roleCode);

    /*查看角色详情*/
    Role roleInfo(String roleCode);

    /*根据role_id查询信息*/
    Role roleInfoById(int roleId);

    /*删除角色同时删除角色用户和被策略使用的信息*/
    int delRole(int roleId);

    /*编辑角色信息*/
    int updateRoleInfo(int id, String roleCode, String roleDescribe);
}
