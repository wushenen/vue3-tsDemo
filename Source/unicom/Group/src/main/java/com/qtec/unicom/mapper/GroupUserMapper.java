package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.GroupUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupUserMapper {

    /*可批量添加分组成员*/
    int addGroupUser(@Param("deviceId") int deviceId, @Param("groupId") int groupId);

    /*判断成员是否已添加*/
    int groupUserExist(@Param("deviceId") int deviceId, @Param("groupId") int groupId);

    /*展示该分组所有成员*/
    List<GroupUserDTO> groupUserList(@Param("groupId") int groupId);

    /*删除成员*/
    int deleteGroupUser(@Param("deviceId") int deviceId, @Param("groupId") int groupId);
}
