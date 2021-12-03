package com.qtec.unicom.service;

import com.qtec.unicom.controller.vo.DeviceStatusDataResponse;
import com.qtec.unicom.pojo.DeviceStatus;

import java.util.List;

public interface DeviceStatusService {

    int updateDeviceStatusInfo(DeviceStatus deviceStatus);

    int addDeviceStatusInfo(DeviceStatus deviceStatus);

    List<DeviceStatus> listDeviceStatusInfo();

    DeviceStatusDataResponse getStatusShowInfo();

    void checkDeviceStatus(String deviceName,int operation);

}
