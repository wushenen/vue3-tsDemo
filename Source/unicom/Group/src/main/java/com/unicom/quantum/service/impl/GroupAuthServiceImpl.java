package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.GroupAuthMapper;
import com.unicom.quantum.pojo.DTO.GroupAuthInfo;
import com.unicom.quantum.service.GroupAuthService;
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
    public String addGroupAuth(Integer groupId, List<Integer> apiId) {
        for (Integer api : apiId) {
            if (groupAuthMapper.existGroupAuth(groupId,api))
                return groupAuthMapper.getApiName(api);
        }
        for (Integer api : apiId) {
            groupAuthMapper.addGroupAuth(groupId,api);
        }
        return "0";
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
