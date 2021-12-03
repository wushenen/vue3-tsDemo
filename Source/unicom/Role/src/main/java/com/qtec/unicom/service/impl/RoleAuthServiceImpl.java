package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.mapper.RoleAuthMapper;
import com.qtec.unicom.pojo.DTO.RoleAuthInfo;
import com.qtec.unicom.service.RoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAuthServiceImpl implements RoleAuthService {

    private final String OPERATE_MODEL = "角色模块";

    @Autowired
    private RoleAuthMapper roleAuthMapper;

    @OperateLogAnno(operateDesc = "添加分组资源权限", operateModel = OPERATE_MODEL)
    @Override
    public int addRoleAuth(Integer roleId, List<Integer> apiId) {
        for (int i = 0; i < apiId.size(); i++) {
            if (roleAuthMapper.existRoleAuth(roleId,apiId.get(i)))
                return apiId.indexOf(i);
        }
        for (Integer api : apiId) {
            roleAuthMapper.addRoleAuth(roleId,api);
        }
        return 0;
    }

    @OperateLogAnno(operateDesc = "撤销分组所有权限", operateModel = OPERATE_MODEL)
    @Override
    public int deleteRoleAuthByRoleId(Integer roleId) {
        return roleAuthMapper.deleteRoleAuthByRoleId(roleId);
    }

    @OperateLogAnno(operateDesc = "撤销该分组部分权限", operateModel = OPERATE_MODEL)
    @Override
    public int deleteRoleAuthById(int roleAuthId) {
        return roleAuthMapper.deleteRoleAuthById(roleAuthId);
    }

    @Override
    public List<RoleAuthInfo> getRoleAuth(Integer roleId) {
        return roleAuthMapper.getRoleAuth(roleId);
    }
}
