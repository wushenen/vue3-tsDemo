package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.GroupDeviceUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupDeviceUserMapper {

    /*可批量添加分组成员*/
    int addGroupDeviceUser(@Param("deviceId") int deviceId, @Param("groupId") int groupId);

    /*判断成员是否已添加*/
    boolean existAddGroupDeviceUser(@Param("deviceId") int deviceId, @Param("groupId") int groupId);

    /*展示该分组所有成员*/
    List<GroupDeviceUserInfo> groupDeviceUserList(@Param("groupId") int groupId);

    /*删除成员*/
    int deleteGroupDeviceUser(List<Integer> ids);
}
