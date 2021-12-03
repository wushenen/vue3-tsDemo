package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.mapper.RoleAppUserMapper;
import com.qtec.unicom.pojo.DTO.RoleAppUserInfo;
import com.qtec.unicom.service.RoleAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAppUserServiceImpl implements RoleAppUserService {

    private final String OPERATE_MODEL = "角色模块";

    @Autowired
    private RoleAppUserMapper roleAppUserMapper;

    @OperateLogAnno(operateDesc = "角色添加应用用户", operateModel = OPERATE_MODEL)
    @Override
    public int addRoleAppUser(List<Integer> appUserIds, int roleId) {
        for (Integer appUserId : appUserIds) {
            if (!roleAppUserMapper.existAddRoleAppUser(appUserId,roleId))
                roleAppUserMapper.addRoleAppUser(appUserId,roleId);
        }
        return 0;
    }

    @OperateLogAnno(operateDesc = "角色删除应用用户", operateModel = OPERATE_MODEL)
    @Override
    public int deleteRoleAppUser(List<Integer> ids) {
        roleAppUserMapper.deleteRoleAppUser(ids);
        return 0;
    }

    @OperateLogAnno(operateDesc = "获取角色应用用户", operateModel = OPERATE_MODEL)
    @Override
    public List<RoleAppUserInfo> roleAppUserList(int roleId) {
        return roleAppUserMapper.roleAppUserList(roleId);
    }
}
