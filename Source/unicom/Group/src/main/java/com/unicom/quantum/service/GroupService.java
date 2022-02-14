package com.unicom.quantum.service;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.pojo.Group;

import java.util.List;

public interface GroupService {

    int addGroup(String groupName, String groupCode, String groupDescribe) throws QuantumException;

    List<Group> groupList();

    int updateGroupInfo(int groupId, String groupName, String groupDescribe) throws QuantumException;

    Group getGroupInfo(int groupId);

    int deleteGroup(int groupId);

    List<Group> queryGroupInfo(String groupName);
}
