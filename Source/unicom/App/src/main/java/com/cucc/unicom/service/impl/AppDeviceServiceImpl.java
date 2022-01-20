package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.controller.vo.AppDeviceRequest;
import com.cucc.unicom.mapper.AppDeviceMapper;
import com.cucc.unicom.pojo.dto.AppDeviceDTO;
import com.cucc.unicom.service.AppDeviceService;
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
