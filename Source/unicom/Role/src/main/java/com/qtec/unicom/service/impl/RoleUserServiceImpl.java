/*
package com.qtec.mixedquantum.service.impl;

import com.qtec.mixedquantum.component.annotation.OperateLogAnno;
import com.qtec.mixedquantum.mapper.RoleUserMapper;
import com.qtec.mixedquantum.pojo.DTO.RoleUserDTO;
import com.qtec.mixedquantum.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleUserServiceImpl implements RoleUserService {

    private static final String NAME = "应用用户管理";

    @Autowired
    private RoleUserMapper roleUserMapper;

    @Override
    @OperateLogAnno(operateDesc = "添加角色授权用户", operateModel = NAME)
    public int addRoleUser(int deviceId, int roleId) {
        return roleUserMapper.addRoleUser(deviceId, roleId);
    }

    @Override
    public int alreadyAdd(int deviceId, int roleId) {
        return roleUserMapper.alreadyAdd(deviceId, roleId);
    }



    @Override
    @OperateLogAnno(operateDesc = "取消角色授权用户", operateModel = NAME)
    public int cancelRoleUser(int deviceId, int roleId) {
        return roleUserMapper.deleteRoleUser(deviceId,roleId);
    }

    @Override
    @OperateLogAnno(operateDesc = "获取角色已授权用户", operateModel = NAME)
    public List<RoleUserDTO> roleUserList(int roleId) {
        return roleUserMapper.roleUserList(roleId);
    }

    @Override
    @OperateLogAnno(operateDesc = "根据userId查询角色code", operateModel = NAME)
    public List<String> getRoleCode(int deviceId) {
        return roleUserMapper.getRoleCode(deviceId);
    }
}
*/
