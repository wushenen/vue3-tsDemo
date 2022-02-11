package com.unicom.quantum.service;

import com.unicom.quantum.pojo.DeviceStatus;
import com.unicom.quantum.controller.vo.DeviceStatusDataResponse;

import java.util.List;

public interface DeviceStatusService {

    int updateDeviceStatusInfo(DeviceStatus deviceStatus);

    List<DeviceStatus> listDeviceStatusInfo();

    DeviceStatusDataResponse getStatusShowInfo();

    void checkDeviceStatus(String deviceName,int operation);

}
