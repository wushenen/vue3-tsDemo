package com.unicom.quantum.service;

import com.unicom.quantum.pojo.Group;

import java.util.List;

public interface GroupService {
    /*创建分组*/
    int addGroup(String groupName, String groupCode, String groupDescribe);

    /*判断分组名称是否唯一*/
    int groupNameExist(String groupName);

    /*判断分组唯一标识唯一*/
    int groupCodeExist(String groupCode);

    /*查看分组信息*/
    List<Group> groupList();

    /*修改分组信息*/
    int updateGroupInfo(int groupId, String groupName, String groupDescribe);

    /*获取当前分组信息*/
    Group getGroupInfo(int groupId);

    /*删除分组同时删除该分组下的所有用户和授权信息*/
    int deleteGroup(int groupId);

    /*模糊查询分组信息*/
    List<Group> queryGroupInfo(String groupName);
}
