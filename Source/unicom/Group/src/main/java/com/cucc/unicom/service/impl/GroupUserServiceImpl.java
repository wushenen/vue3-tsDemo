/*
package com.qtec.mixedquantum.service.impl;

import com.qtec.mixedquantum.component.annotation.OperateLogAnno;
import com.qtec.mixedquantum.mapper.GroupUserMapper;
import com.qtec.mixedquantum.pojo.DTO.GroupUserDTO;
import com.qtec.mixedquantum.service.GroupUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupUserServiceImpl implements GroupUserService {

    private static final String NAME = "分组管理";

    @Autowired
    private GroupUserMapper groupUserMapper;

    @Override
    @OperateLogAnno(operateDesc = "添加分组成员", operateModel = NAME)
    public int addGroupUser(int deviceId, int groupId) {
        return groupUserMapper.addGroupUser(deviceId, groupId);
    }

    @Override
    public int groupUserExist(int deviceId, int groupId) {
        return groupUserMapper.groupUserExist(deviceId, groupId);
    }

    @Override
    @OperateLogAnno(operateDesc = "展示该分组所有成员", operateModel = NAME)
    public List<GroupUserDTO> groupUserList(int groupId) {
        return groupUserMapper.groupUserList(groupId);
    }

    @Override
    @OperateLogAnno(operateDesc = "删除分组成员", operateModel = NAME)
    public int deleteGroupUser(int deviceId,int groupId) {
        return groupUserMapper.deleteGroupUser(deviceId,groupId);
    }
}
*/
