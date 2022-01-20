package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.mapper.GroupAuthMapper;
import com.cucc.unicom.pojo.DTO.GroupAuthInfo;
import com.cucc.unicom.service.GroupAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupAuthServiceImpl implements GroupAuthService {

    private final String OPERATE_MODEL = "分组模块";

    @Autowired
    private GroupAuthMapper groupAuthMapper;

    @OperateLogAnno(operateDesc = "添加分组资源权限", operateModel = OPERATE_MODEL)
    @Override
    public int addGroupAuth(Integer groupId, List<Integer> apiId) {
        for (int i = 0; i < apiId.size(); i++) {
            if (groupAuthMapper.existGroupAuth(groupId,apiId.get(i)))
                return apiId.indexOf(i);
        }
        for (Integer api : apiId) {
            groupAuthMapper.addGroupAuth(groupId,api);
        }

        return 0;
    }

    @OperateLogAnno(operateDesc = "撤销分组所有权限", operateModel = OPERATE_MODEL)
    @Override
    public int deleteGroupAuthByGroupId(Integer groupId) {
        return groupAuthMapper.deleteGroupAuthByGroupId(groupId);
    }

    @OperateLogAnno(operateDesc = "撤销分组部分权限", operateModel = OPERATE_MODEL)
    @Override
    public int deleteGroupAuthById(Integer groupAuthId) {
        return groupAuthMapper.deleteGroupAuthById(groupAuthId);
    }

    @OperateLogAnno(operateDesc = "查看分组权限", operateModel = OPERATE_MODEL)
    @Override
    public List<GroupAuthInfo> getGroupAuth(Integer groupId) {
        return groupAuthMapper.getGroupAuth(groupId);
    }
}
