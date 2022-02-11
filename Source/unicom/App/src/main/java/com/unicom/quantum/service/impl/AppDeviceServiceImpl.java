package com.unicom.quantum.service.impl;

import com.unicom.quantum.controller.vo.AppDeviceRequest;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.AppDeviceMapper;
import com.unicom.quantum.pojo.dto.AppDeviceDTO;
import com.unicom.quantum.service.AppDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AppDeviceServiceImpl implements AppDeviceService {

    private final String OPERATE_MODEL = "应用模块";

    @Autowired
    private AppDeviceMapper appDeviceMapper;

    @OperateLogAnno(operateDesc = "应用绑定终端", operateModel = OPERATE_MODEL)
    @Override
    public int addAppDevice(AppDeviceRequest appDeviceRequest) {
        boolean flag = false;
        for (Integer deviceId : appDeviceRequest.getDeviceIds()) {
            if (!appDeviceMapper.appDeviceExist(appDeviceRequest.getAppId(),deviceId)) {
                appDeviceMapper.addAppDevice(appDeviceRequest.getAppId(),deviceId);
                String deviceName = appDeviceMapper.getDeviceName(deviceId);
                if (!appDeviceMapper.deviceStatusInfoExist(deviceName)) {
                    appDeviceMapper.addDeviceToDeviceStatus(deviceName);
                }
            }else {
                flag = true;
            }
        }
        if (flag) return 1;
        return 0;
    }

    @OperateLogAnno(operateDesc = "应用解绑终端", operateModel = OPERATE_MODEL)
    @Override
    public int deleteAppDevice(AppDeviceRequest appDeviceRequest) {
        for (Integer deviceId : appDeviceRequest.getDeviceIds()) {
            appDeviceMapper.deleteAppDevice(appDeviceRequest.getAppId(),deviceId);
        }
        return 0;
    }

    @OperateLogAnno(operateDesc = "查看应用绑定终端信息", operateModel = OPERATE_MODEL)
    @Override
    public List<AppDeviceDTO> getAppDevice(int appId) {
        return appDeviceMapper.getAppDevice(appId);
    }

}
