package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.GroupAuthMapper;
import com.unicom.quantum.mapper.GroupMapper;
import com.unicom.quantum.pojo.Group;
import com.unicom.quantum.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final String OPERATE_MODEL = "分组模块";

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupAuthMapper groupAuthMapper;

    @Override
    @OperateLogAnno(operateDesc = "创建分组", operateModel = OPERATE_MODEL)
    public int addGroup(String groupName, String groupCode, String groupDescribe) {
        return groupMapper.addGroup(groupName, groupCode, groupDescribe);
    }

    @Override
    public int groupNameExist(String groupName) {
        return groupMapper.groupNameExist(groupName);
    }

    @Override
    public int groupCodeExist(String groupCode) {
        return groupMapper.groupCodeExist(groupCode);
    }

    @Override
    @OperateLogAnno(operateDesc = "查看分组信息", operateModel = OPERATE_MODEL)
    public List<Group> groupList() {
        return groupMapper.groupList();
    }

    @Override
    @OperateLogAnno(operateDesc = "修改分组信息", operateModel = OPERATE_MODEL)
    public int updateGroupInfo(int groupId, String groupName, String groupDescribe) {
        return groupMapper.updateGroupInfo(groupId, groupName, groupDescribe);
    }

    @Override
    public Group getGroupInfo(int groupId) {
        return groupMapper.getGroupInfo(groupId);
    }

    @Override
    @OperateLogAnno(operateDesc = "删除分组", operateModel = OPERATE_MODEL)
    public int deleteGroup(int groupId) {
        groupMapper.deleteGroupDeviceUser(groupId);
        groupAuthMapper.deleteGroupAuthByGroupId(groupId);
        return groupMapper.deleteGroup(groupId);
    }

    @Override
    public List<Group> queryGroupInfo(String groupName) {
        return groupMapper.queryGroupInfo(groupName);
    }
}
