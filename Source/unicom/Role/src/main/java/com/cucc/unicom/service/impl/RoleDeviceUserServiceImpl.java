package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.mapper.RoleDeviceUserMapper;
import com.cucc.unicom.pojo.DTO.RoleDeviceUserInfo;
import com.cucc.unicom.service.RoleDeviceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleDeviceUserServiceImpl implements RoleDeviceUserService {

    private final String OPERATE_MODEL = "角色模块";

    @Autowired
    private RoleDeviceUserMapper roleDeviceUserMapper;

    @OperateLogAnno(operateDesc = "添加角色终端用户", operateModel = OPERATE_MODEL)
    @Override
    public int addRoleDeviceUser(List<Integer> deviceIds, int roleId) {
        for (Integer deviceId : deviceIds) {
            if (!roleDeviceUserMapper.existAddRoleDeviceUser(deviceId,roleId))
                roleDeviceUserMapper.addRoleDeviceUser(deviceId,roleId);
        }
        return 0;
    }

    @OperateLogAnno(operateDesc = "删除分组终端用户", operateModel = OPERATE_MODEL)
    @Override
    public int deleteRoleDeviceUser(List<Integer> ids) {
        roleDeviceUserMapper.deleteRoleDeviceUser(ids);
        return 0;
    }

    @OperateLogAnno(operateDesc = "获取分组终端用户", operateModel = OPERATE_MODEL)
    @Override
    public List<RoleDeviceUserInfo> roleDeviceUserList(int roleId) {
        return roleDeviceUserMapper.roleDeviceUserList(roleId);
    }
}
