package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.DTO.GroupAppUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupAppUserMapper {

    /*可批量添加分组成员*/
    int addGroupAppUser(@Param("appUserId") int appUserId, @Param("groupId") int groupId);

    /*判断成员是否已添加*/
    boolean existAddGroupAppUser(@Param("appUserId") int appUserId, @Param("groupId") int groupId);

    /*展示该分组所有成员*/
    List<GroupAppUserInfo> groupAppUserList(@Param("groupId") int groupId);

    /*删除成员*/
    int deleteGroupAppUser(List<Integer> ids);
}
