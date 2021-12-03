package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.StrategyActionInfo;
import com.qtec.unicom.pojo.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StrategyMapper {

    /*添加策略*/
    int addStrategy(String strategyName, int strategyType, String strategyDescribe);
    /*判断策略名称是否重复*/
    boolean strategyNameExist(String strategyName);

    /*删除策略*/
    int delStrategy(int strategyId);
    /*同时删除策略的授权信息*/
    int delStrategyAuthByStrategyId(int strategyId);
    /*同时删除策略的操作信息*/
    int delStrategyActionByStrategyId(int strategyId);

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
