package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.RoleAuthInfo;

import java.util.List;

public interface RoleAuthService {

    int addRoleAuth(Integer roleId, List<Integer> apiId);

    int deleteRoleAuthByRoleId(Integer roleId);

    int deleteRoleAuthById(int roleAuthId);

    List<RoleAuthInfo> getRoleAuth(Integer roleId);
}
