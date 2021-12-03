package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.StrategyAuthInfo;

import java.util.List;

public interface StrategyAuthorityService {

    /*添加策略授权信息*/
    int addStrategyAuth(Integer deviceId, Integer roleId, Integer groupId, Integer appUserId, int strategyId);
    /*判断策略是否重复授权*/
    boolean strategyIsAdd(Integer deviceId, Integer roleId, Integer groupId, Integer appUserId, int strategyId);

    /*删除策略授权信息*/
    int delStrategyAuth(int strategyAuthId);

    /*获取策略授权信息*/
    List<StrategyAuthInfo> getStrategyAuthInfo();
}
