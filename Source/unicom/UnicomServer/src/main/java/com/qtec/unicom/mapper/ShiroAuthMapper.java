package com.qtec.unicom.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShiroAuthMapper {

    //根据appUserId获取角色信息
    @Select("select role_id from t_role_app_user where app_user_id = #{appUserId}")
    List<Integer> getRoleInfosByAppUserId(int appUserId);
    //根据appUserId获取分组信息
    @Select("select group_id from t_group_app_user where app_user_id = #{appUserId}")
    List<Integer> getGroupInfosByAppUserId(int appUserId);
    //根据deviceId获取角色信息
    @Select("select role_id from t_role_device_user where device_id = #{deviceId}")
    List<Integer> getRoleInfosByDeviceId(int deviceId);
    //根据deviceId获取分组信息
    @Select("select group_id from t_group_device_user where device_id = #{deviceId}")
    List<Integer> getGroupInfosByDeviceId(int deviceId);

    //获取策略授权信息
    @Select("select strategy_id from t_strategy_auth where device_id = #{deviceId}")
    List<Integer> getStrategyIdByDeviceId(int deviceId);
    @Select("select strategy_id from t_strategy_auth where app_user_id = #{appUserId}")
    List<Integer> getStrategyIdByAppUserId(int appUserId);
    @Select("select strategy_id from t_strategy_auth where role_id = #{roleId}")
    List<Integer> getStrategyIdByRoleId(int RoleId);
    @Select("select strategy_id from t_strategy_auth where group_id = #{groupId}")
    List<Integer> getStrategyIdByGroupId(int GroupId);
    @Select("select strategy_id from t_strategy_action where strategy_id = #{strategyId}")
    List<Integer> getApiIdByStrategyId(int StrategyId);


}
