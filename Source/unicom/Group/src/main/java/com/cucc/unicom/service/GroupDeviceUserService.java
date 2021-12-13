package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DTO.GroupDeviceUserInfo;

import java.util.List;

public interface GroupDeviceUserService {

    int addGroupDeviceUser(List<Integer> deviceIds, int groupId);

    List<GroupDeviceUserInfo> groupDeviceUserList(int groupId);

    int deleteGroupDeviceUser(List<Integer> ids);
}
