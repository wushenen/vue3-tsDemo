package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.StrategyMapper;
import com.cucc.unicom.pojo.DTO.StrategyActionInfo;
import com.cucc.unicom.pojo.Strategy;
import com.cucc.unicom.service.StrategyService;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyServiceImpl implements StrategyService {

    private final String OPERATE_MODEL = "策略信息模块";

    @Autowired
    private StrategyMapper strategyMapper;

    @OperateLogAnno(operateDesc = "添加策略信息", operateModel = OPERATE_MODEL)
    @Override
    public int addStrategy(String strategyName, int strategyType, String strategyDescribe) {
        return strategyMapper.addStrategy(strategyName, strategyType, strategyDescribe);
    }

    @Override
    public boolean strategyNameExist(String strategyName) {
        return strategyMapper.strategyNameExist(strategyName);
    }


    @OperateLogAnno(operateDesc = "删除策略信息", operateModel = OPERATE_MODEL)
    @Override
    public int delStrategy(int strategyId) {
        strategyMapper.delStrategyActionByStrategyId(strategyId);
        strategyMapper.delStrategyAuthByStrategyId(strategyId);
        return strategyMapper.delStrategy(strategyId);
    }


    @OperateLogAnno(operateDesc = "修改策略信息", operateModel = OPERATE_MODEL)
    @Override
    public int updateStrategy(int strategyId, String strategyName, String strategyDescribe) {
        return strategyMapper.updateStrategy(strategyId, strategyName, strategyDescribe);
    }


    @OperateLogAnno(operateDesc = "获取所有策略信息", operateModel = OPERATE_MODEL)
    @Override
    public List<Strategy> getStrategyList() {
        return strategyMapper.getStrategyList();
    }


    @OperateLogAnno(operateDesc = "获取指定策略信息", operateModel = OPERATE_MODEL)
    @Override
    public Strategy getStrategyInfo(int strategyId) {
        return strategyMapper.getStrategyInfo(strategyId);
    }


    @OperateLogAnno(operateDesc = "添加策略操作", operateModel = OPERATE_MODEL)
    @Override
    public int addStrategyAction(int strategyId, int apiId) {
        if (!strategyMapper.actionExist(strategyId, apiId)) {
            strategyMapper.addStrategyAction(strategyId, apiId);
        }
        return 0;
    }

    @Override
    public boolean actionExist(int strategyId, int apiId) {
        return strategyMapper.actionExist(strategyId, apiId);
    }

    @OperateLogAnno(operateDesc = "删除策略操作", operateModel = OPERATE_MODEL)
    @Override
    public int delStrategyAction(int strategyActionId) {
        return strategyMapper.delStrategyAction(strategyActionId);
    }

    @OperateLogAnno(operateDesc = "获取指定策略可操作信息", operateModel = OPERATE_MODEL)
    @Override
    public List<StrategyActionInfo> getStrategyActionInfo(int strategyId) {
        return strategyMapper.getStrategyActionInfo(strategyId);
    }
}
