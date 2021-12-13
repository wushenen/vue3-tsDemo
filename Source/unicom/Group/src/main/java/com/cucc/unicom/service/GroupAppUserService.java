package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DTO.GroupAppUserInfo;

import java.util.List;

public interface GroupAppUserService {

    int addGroupAppUser(List<Integer> appUserIds, int groupId);

    List<GroupAppUserInfo> groupAppUserList(int groupId);

    int deleteGroupAppUser(List<Integer> ids);
}
