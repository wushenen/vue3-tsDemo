package com.unicom.quantum.mapper;


import com.unicom.quantum.pojo.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupMapper {

    /*创建分组*/
    int addGroup(@Param("groupName") String groupName, @Param("groupCode") String groupCode, @Param("groupDescribe") String groupDescribe);

    /*判断分组名称是否唯一*/
    int groupNameExist(@Param("groupName") String groupName);

    /*判断分组唯一标识唯一*/
    int groupCodeExist(@Param("groupCode") String groupCode);

    /*获取分组信息*/
    List<Group> groupList();

    /*修改分组信息*/
    int updateGroupInfo(@Param("groupId") int groupId, @Param("groupName") String groupName, @Param("groupDescribe") String groupDescribe);

    /*获取当前分组名称信息*/
    Group getGroupInfo(@Param("groupId") int groupId);

    /*删除分组*/
    int deleteGroup(@Param("groupId") int groupId);
    /*同时删除该分组下的所有用户*/
    int deleteGroupDeviceUser(@Param("groupId") int groupId);

    /*模糊查询分组信息*/
    List<Group> queryGroupInfo(String groupName);



}
