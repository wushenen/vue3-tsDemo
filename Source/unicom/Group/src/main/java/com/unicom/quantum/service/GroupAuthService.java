package com.unicom.quantum.service;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.pojo.DTO.GroupAuthInfo;

import java.util.List;

public interface GroupAuthService {

    int addGroupAuth(Integer groupId, List<Integer> apiId) throws QuantumException;

    int deleteGroupAuthByGroupId(Integer groupId);

    int deleteGroupAuthById(Integer groupAuthId);

    List<GroupAuthInfo> getGroupAuth(Integer groupId);
}
