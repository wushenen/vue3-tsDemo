package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DTO.GroupAuthInfo;

import java.util.List;

public interface GroupAuthService {

    String addGroupAuth(Integer groupId, List<Integer> apiId);

    int deleteGroupAuthByGroupId(Integer groupId);

    int deleteGroupAuthById(Integer groupAuthId);

    List<GroupAuthInfo> getGroupAuth(Integer groupId);
}
