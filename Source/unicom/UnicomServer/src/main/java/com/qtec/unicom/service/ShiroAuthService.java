package com.qtec.unicom.service;

import java.util.List;

public interface ShiroAuthService {

    //根据appUserId获取角色信息
    List<Integer> getRoleInfosByAppUserId(int appUserId);
    //根据appUserId获取分组信息
    List<Integer> getGroupInfosByAppUserId(int appUserId);
    //根据deviceId获取角色信息
    List<Integer> getRoleInfosByDeviceId(int deviceId);
    //根据deviceId获取分组信息
    List<Integer> getGroupInfosByDeviceId(int deviceId);

    List<Integer> getStrategyIdByDeviceId(int deviceId);
    List<Integer> getStrategyIdByAppUserId(int appUserId);
    List<Integer> getStrategyIdByRoleId(int roleId);
    List<Integer> getStrategyIdByGroupId(int groupId);
    List<Integer> getApiIdByStrategyId(int strategyId);
}
