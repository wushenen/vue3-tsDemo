package com.qtec.unicom.service;

import com.qtec.unicom.pojo.DTO.AuthInfo;

import java.util.List;

public interface DeviceAuthorityService {

    /*添加用户授权*/
    int addDeviceAuthority(int deviceId, int apiId);
    /*判断权限是否已被添加*/
    boolean deviceAuthorityIsAdd(int deviceId, int apiId);

    /*删除用户授权（可批量）*/
    int delDeviceAuthority(int authId);

    /*删除用户所有权限*/
    int delDeviceAuthByDeviceId(int deviceId);

    /*获取指定用户已添加权限信息*/
    List<AuthInfo> getDeviceAuthority(int deviceId);

}
