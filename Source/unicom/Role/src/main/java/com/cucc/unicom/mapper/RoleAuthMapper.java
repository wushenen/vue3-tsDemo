package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.DTO.RoleAuthInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleAuthMapper {

    int addRoleAuth(Integer roleId, Integer apiId);

    boolean existRoleAuth(Integer roleId, Integer apiId);

    //删除角色时，将该角色下的授权信息全部删除
    int deleteRoleAuthByRoleId(Integer roleId);

    int deleteRoleAuthById(int roleAuthId);

    List<RoleAuthInfo> getRoleAuth(Integer roleId);

}
