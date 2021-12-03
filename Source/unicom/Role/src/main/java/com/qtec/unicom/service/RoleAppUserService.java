package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.RoleAppUserInfo;

import java.util.List;

public interface RoleAppUserService {

    int addRoleAppUser(List<Integer> appUserIds, int roleId);

    int deleteRoleAppUser(List<Integer> ids);

    List<RoleAppUserInfo> roleAppUserList(int roleId);
}
