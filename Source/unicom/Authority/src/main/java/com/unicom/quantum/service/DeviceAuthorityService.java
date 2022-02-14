package com.unicom.quantum.service;

import com.unicom.quantum.pojo.DTO.AuthInfo;

import java.util.List;

public interface DeviceAuthorityService {

    int addDeviceAuthority(int deviceId, int apiId);
    int delDeviceAuthority(int authId);
    int delDeviceAuthByDeviceId(int deviceId);
    List<AuthInfo> getDeviceAuthority(int deviceId);

}
