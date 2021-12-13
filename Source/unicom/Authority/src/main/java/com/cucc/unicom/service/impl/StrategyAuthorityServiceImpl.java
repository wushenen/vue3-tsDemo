package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.StrategyAuthorityMapper;
import com.cucc.unicom.pojo.DTO.StrategyAuthInfo;
import com.cucc.unicom.service.StrategyAuthorityService;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyAuthorityServiceImpl implements StrategyAuthorityService {

    private final String OPERATE_MODEL = "策略授权模块";

    @Autowired
    private StrategyAuthorityMapper strategyAuthorityMapper;

    @OperateLogAnno(operateDesc = "添加策略权限", operateModel = OPERATE_MODEL)
    @Override
    public int addStrategyAuth(Integer deviceId, Integer roleId, Integer groupId, Integer appUserId, int strategyId) {
        return strategyAuthorityMapper.addStrategyAuth(deviceId, roleId, groupId, appUserId, strategyId);
    }

    @Override
    public boolean strategyIsAdd(Integer deviceId, Integer roleId, Integer groupId, Integer appUserId, int strategyId) {
        return strategyAuthorityMapper.strategyIsAdd(deviceId, roleId, groupId, appUserId, strategyId);
    }

    @OperateLogAnno(operateDesc = "删除策略权限", operateModel = OPERATE_MODEL)
    @Override
    public int delStrategyAuth(int strategyAuthId) {
        return strategyAuthorityMapper.delStrategyAuth(strategyAuthId);
    }

    @OperateLogAnno(operateDesc = "获取所有策略权限", operateModel = OPERATE_MODEL)
    @Override
    public List<StrategyAuthInfo> getStrategyAuthInfo() {
        return strategyAuthorityMapper.getStrategyAuthInfo();
    }
}
