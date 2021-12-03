package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.StrategyAuthInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StrategyAuthorityMapper {

    /*添加策略授权信息*/
    int addStrategyAuth(Integer deviceId, Integer roleId, Integer groupId, Integer appUserId, int strategyId);
    /*判断策略是否重复授权*/
    boolean strategyIsAdd(Integer deviceId, Integer roleId, Integer groupId, Integer appUserId, int strategyId);

    /*删除策略授权信息*/
    int delStrategyAuth(int strategyAuthId);

    /*获取策略授权信息*/
    List<StrategyAuthInfo> getStrategyAuthInfo();

}
