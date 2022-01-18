package com.cucc.unicom.service;

import com.cucc.unicom.controller.vo.AppDeviceRequest;
import com.cucc.unicom.pojo.dto.AppDeviceDTO;

import java.util.List;

public interface AppDeviceService {

    int addAppDevice(AppDeviceRequest appDeviceRequest);
    int deleteAppDevice(AppDeviceRequest appDeviceRequest);
    List<AppDeviceDTO> getAppDevice(int appId);

}
