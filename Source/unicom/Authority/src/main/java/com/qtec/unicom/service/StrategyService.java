package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.StrategyActionInfo;
import com.qtec.unicom.pojo.Strategy;

import java.util.List;

public interface StrategyService {

    /*添加策略*/
    int addStrategy(String strategyName, int strategyType, String strategyDescribe);
    /*判断策略名称是否重复*/
    boolean strategyNameExist(String strategyName);

    /*删除策略*/
    int delStrategy(int strategyId);

    /*修改策略信息*/
    int updateStrategy(int strategyId, String strategyName, String strategyDescribe);

    /*获取所有策略信息*/
    List<Strategy> getStrategyList();

    /*获取指定策略详细信息*/
    Strategy getStrategyInfo(int strategyId);

    /*添加策略操作*/
    int addStrategyAction(int strategyId, int apiId);
    /*判断操作是否已添加至该操作下*/
    boolean actionExist(int strategyId, int apiId);

    /*删除策略操作*/
    int delStrategyAction(int strategyActionId);

    /*查询当前策略包含的操作*/
    List<StrategyActionInfo> getStrategyActionInfo(int strategyId);

}
