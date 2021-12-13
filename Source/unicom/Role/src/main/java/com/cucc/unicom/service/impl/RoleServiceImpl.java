package com.cucc.unicom.service.impl;


import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.mapper.RoleMapper;
import com.cucc.unicom.pojo.Role;
import com.cucc.unicom.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final String OPERATE_MODEL = "角色模块";

    @Autowired
    private RoleMapper roleMapper;

    @Override
    @OperateLogAnno(operateDesc = "创建角色", operateModel = OPERATE_MODEL)
    public int addRole(String roleCode, String roleDescribe) {
        return roleMapper.addRole(roleCode, roleDescribe);
    }

    @Override
    public int uniqueRole(String roleCode) {
        return roleMapper.uniqueRole(roleCode);
    }

    @Override
    @OperateLogAnno(operateDesc = "查看所有角色code信息", operateModel = OPERATE_MODEL)
    public List<Role> roleList() {
        return roleMapper.roleList();
    }

    @Override
    public List<Role> searchRole(String roleCode) {
        return roleMapper.searchRole(roleCode);
    }

    @Override
    @OperateLogAnno(operateDesc = "查看角色详情", operateModel = OPERATE_MODEL)
    public Role roleInfo(String roleCode) {
        return roleMapper.roleInfo(roleCode);
    }

    @Override
    @OperateLogAnno(operateDesc = "根据roleId查询信息", operateModel = OPERATE_MODEL)
    public Role roleInfoById(int roleId) {
        return roleMapper.roleInfoById(roleId);
    }

    @Override
    @OperateLogAnno(operateDesc = "删除角色", operateModel = OPERATE_MODEL)
    public int delRole(int roleId) {
        roleMapper.delRoleUser(roleId);
        roleMapper.delStrategyAuthByRoleId(roleId);
        return roleMapper.delRole(roleId);
    }

    @Override
    @OperateLogAnno(operateDesc = "更新角色信息", operateModel = OPERATE_MODEL)
    public int updateRoleInfo(int id, String roleCode, String roleDescribe) {
        return roleMapper.updateRoleInfo(id, roleCode, roleDescribe);
    }
}
