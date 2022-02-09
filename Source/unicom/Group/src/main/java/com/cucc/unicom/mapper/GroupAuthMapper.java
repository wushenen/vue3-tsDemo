package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.DTO.GroupAuthInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupAuthMapper {

    int addGroupAuth(Integer groupId, Integer apiId);

    boolean existGroupAuth(Integer groupId, Integer apiId);

    int deleteGroupAuthByGroupId(Integer groupId);

    int deleteGroupAuthById(Integer groupAuthId);

    List<GroupAuthInfo> getGroupAuth(Integer groupId);

    String getApiName(Integer apiId);

}
