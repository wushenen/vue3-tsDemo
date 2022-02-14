package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.DTO.GroupDeviceUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupDeviceUserMapper {

    int addGroupDeviceUser(@Param("deviceId") int deviceId, @Param("groupId") int groupId);

    boolean existAddGroupDeviceUser(@Param("deviceId") int deviceId, @Param("groupId") int groupId);

    List<GroupDeviceUserInfo> groupDeviceUserList(@Param("groupId") int groupId);

    int deleteGroupDeviceUser(List<Integer> ids);
}
