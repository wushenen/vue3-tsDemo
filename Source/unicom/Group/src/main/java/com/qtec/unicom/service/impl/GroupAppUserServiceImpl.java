package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.mapper.GroupAppUserMapper;
import com.qtec.unicom.pojo.DTO.GroupAppUserInfo;
import com.qtec.unicom.service.GroupAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupAppUserServiceImpl implements GroupAppUserService {

    private final String OPERATE_MODEL = "分组模块";

    @Autowired
    private GroupAppUserMapper groupAppUserMapper;

    @OperateLogAnno(operateDesc = "分组添加应用用户", operateModel = OPERATE_MODEL)
    @Override
    public int addGroupAppUser(List<Integer> appUserIds, int groupId) {
        for (Integer appUserId : appUserIds) {
            if (!groupAppUserMapper.existAddGroupAppUser(appUserId,groupId))
                groupAppUserMapper.addGroupAppUser(appUserId,groupId);
        }
        return 0;
    }

    @OperateLogAnno(operateDesc = "获取分组应用用户信息", operateModel = OPERATE_MODEL)
    @Override
    public List<GroupAppUserInfo> groupAppUserList(int groupId) {
        return groupAppUserMapper.groupAppUserList(groupId);
    }


    @OperateLogAnno(operateDesc = "分组删除应用用户", operateModel = OPERATE_MODEL)
    @Override
    public int deleteGroupAppUser(List<Integer> ids) {
        return groupAppUserMapper.deleteGroupAppUser(ids);
    }
}
