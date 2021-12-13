package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DeviceStatus;
import com.cucc.unicom.controller.vo.DeviceStatusDataResponse;

import java.util.List;

public interface DeviceStatusService {

    int updateDeviceStatusInfo(DeviceStatus deviceStatus);

    int addDeviceStatusInfo(DeviceStatus deviceStatus);

    List<DeviceStatus> listDeviceStatusInfo();

    DeviceStatusDataResponse getStatusShowInfo();

    void checkDeviceStatus(String deviceName,int operation);

}
