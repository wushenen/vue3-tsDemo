package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.ShiroAuthMapper;
import com.cucc.unicom.service.ShiroAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShiroAuthServiceImpl implements ShiroAuthService {

    @Autowired
    private ShiroAuthMapper shiroAuthMapper;

    @Override
    public List<Integer> getRoleInfosByAppUserId(int appUserId) {
        return shiroAuthMapper.getRoleInfosByAppUserId(appUserId);
    }

    @Override
    public List<Integer> getGroupInfosByAppUserId(int appUserId) {
        return shiroAuthMapper.getGroupInfosByAppUserId(appUserId);
    }

    @Override
    public List<Integer> getRoleInfosByDeviceId(int deviceId) {
        return shiroAuthMapper.getRoleInfosByDeviceId(deviceId);
    }

    @Override
    public List<Integer> getGroupInfosByDeviceId(int deviceId) {
        return shiroAuthMapper.getGroupInfosByDeviceId(deviceId);
    }

    @Override
    public List<Integer> getStrategyIdByDeviceId(int deviceId) {
        return shiroAuthMapper.getStrategyIdByDeviceId(deviceId);
    }

    @Override
    public List<Integer> getStrategyIdByAppUserId(int appUserId) {
        return shiroAuthMapper.getStrategyIdByAppUserId(appUserId);
    }

    @Override
    public List<Integer> getStrategyIdByRoleId(int roleId) {
        return shiroAuthMapper.getStrategyIdByRoleId(roleId);
    }

    @Override
    public List<Integer> getStrategyIdByGroupId(int groupId) {
        return shiroAuthMapper.getStrategyIdByGroupId(groupId);
    }

    @Override
    public List<Integer> getApiIdByStrategyId(int strategyId) {
        return shiroAuthMapper.getApiIdByStrategyId(strategyId);
    }
}
