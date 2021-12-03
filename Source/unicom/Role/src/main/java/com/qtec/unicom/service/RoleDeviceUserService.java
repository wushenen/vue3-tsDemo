package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.RoleDeviceUserInfo;

import java.util.List;

public interface RoleDeviceUserService {

    int addRoleDeviceUser(List<Integer> deviceIds, int roleId);

    int deleteRoleDeviceUser(List<Integer> ids);

    List<RoleDeviceUserInfo> roleDeviceUserList(int roleId);
}
