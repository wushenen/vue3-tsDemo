package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DTO.AuthInfo;

import java.util.List;

public interface AppAuthorityService {

    /*添加用户授权*/
    int addAppAuthority(int appUserId, List<Integer> apiIds);

    /*删除用户授权（可批量）*/
    int delAppAuthority(int authId);

    /*删除用户所有权限*/
    int delAppAuthByAppUserId(int appUserId);

    /*获取指定用户已添加权限信息*/
    List<AuthInfo> getAppAuthority(int appUserId);

}
