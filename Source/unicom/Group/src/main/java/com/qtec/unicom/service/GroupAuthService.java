package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.GroupAuthInfo;

import java.util.List;

public interface GroupAuthService {

    int addGroupAuth(Integer groupId, List<Integer> apiId);

    int deleteGroupAuthByGroupId(Integer groupId);

    int deleteGroupAuthById(Integer groupAuthId);

    List<GroupAuthInfo> getGroupAuth(Integer groupId);
}
