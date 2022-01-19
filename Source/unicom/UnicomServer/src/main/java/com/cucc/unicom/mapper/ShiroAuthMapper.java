package com.cucc.unicom.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShiroAuthMapper {

    //根据deviceId获取分组信息
    @Select("select group_id from t_group_device_user where device_id = #{deviceId}")
    List<Integer> getGroupInfosByDeviceId(int deviceId);

}
