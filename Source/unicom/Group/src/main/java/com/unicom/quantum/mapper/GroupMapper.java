package com.unicom.quantum.mapper;


import com.unicom.quantum.pojo.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupMapper {

    int addGroup(@Param("groupName") String groupName, @Param("groupCode") String groupCode, @Param("groupDescribe") String groupDescribe);

    boolean groupNameExist(@Param("groupName") String groupName);

    boolean groupCodeExist(@Param("groupCode") String groupCode);

    List<Group> groupList();

    int updateGroupInfo(@Param("groupId") int groupId, @Param("groupName") String groupName, @Param("groupDescribe") String groupDescribe);

    Group getGroupInfo(@Param("groupId") int groupId);

    int deleteGroup(@Param("groupId") int groupId);

    int deleteGroupDeviceUser(@Param("groupId") int groupId);

    List<Group> queryGroupInfo(String groupName);

}
