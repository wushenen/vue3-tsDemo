package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.AppDeviceRequest;
import com.unicom.quantum.pojo.dto.AppDeviceDTO;

import java.util.List;

public interface AppDeviceService {

    int addAppDevice(AppDeviceRequest appDeviceRequest);
    int deleteAppDevice(AppDeviceRequest appDeviceRequest);
    List<AppDeviceDTO> getAppDevice(int appId);

}
