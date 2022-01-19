package com.cucc.unicom.service;

import com.cucc.unicom.controller.vo.DeviceStatusDataResponse;
import com.cucc.unicom.pojo.DeviceStatus;

import java.util.List;

public interface AppStatusService {
    DeviceStatusDataResponse getCurrentAppStatus(int appId);
    List<DeviceStatus> listDeviceStatusInfo(int appId);
}
