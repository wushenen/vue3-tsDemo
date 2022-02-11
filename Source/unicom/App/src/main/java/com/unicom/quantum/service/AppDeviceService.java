package com.unicom.quantum.service;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.controller.vo.AppDeviceRequest;
import com.unicom.quantum.pojo.dto.AppDeviceDTO;

import java.util.List;

public interface AppDeviceService {

    int addAppDevice(AppDeviceRequest appDeviceRequest) throws QuantumException;
    int deleteAppDevice(AppDeviceRequest appDeviceRequest);
    List<AppDeviceDTO> getAppDevice(int appId);

}
