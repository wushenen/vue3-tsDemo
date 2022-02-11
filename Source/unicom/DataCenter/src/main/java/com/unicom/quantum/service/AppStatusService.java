package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.DeviceStatusDataResponse;
import com.unicom.quantum.pojo.DeviceStatus;

import java.util.List;

public interface AppStatusService {
    DeviceStatusDataResponse getCurrentAppStatus(int appId);
    List<DeviceStatus> listDeviceStatusInfo(int appId);
}
