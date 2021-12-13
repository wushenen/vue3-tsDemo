package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.DTO.AuthInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppAuthorityMapper {

    /*添加终端权限*/
    int addAppAuthority(@Param("appUserId") int appUserId, @Param("apiId") int apiId);
    /*判断终端权限是否已被添加*/
    boolean appAuthorityIsAdd(@Param("appUserId") int appUserId, @Param("apiId") int apiId);

    /*删除终端权限（可批量）*/
    int delAppAuthority(@Param("authId") int authId);

    /*删除终端所有权限*/
    int delAppAuthByAppUserId(@Param("appUserId") int appUserId);

    /*获取指定终端已添加权限信息*/
    List<AuthInfo> getAppAuthority(@Param("appUserId") int appUserId);
}
