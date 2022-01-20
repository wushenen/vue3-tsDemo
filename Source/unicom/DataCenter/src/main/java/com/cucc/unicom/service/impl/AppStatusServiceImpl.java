package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.controller.vo.DeviceStatusDataResponse;
import com.cucc.unicom.mapper.AppDeviceMapper;
import com.cucc.unicom.mapper.AppStatusMapper;
import com.cucc.unicom.pojo.DTO.DeviceStatusDataInfo;
import com.cucc.unicom.pojo.DeviceStatus;
import com.cucc.unicom.pojo.dto.AppDeviceDTO;
import com.cucc.unicom.service.AppStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppStatusServiceImpl implements AppStatusService {

    private final String OPERATE_MODEL = "应用数据监测";

    @Autowired
    private AppStatusMapper appStatusMapper;

    @Autowired
    private AppDeviceMapper appDeviceMapper;

    @OperateLogAnno(operateDesc = "查看指定应用汇总状态数据", operateModel = OPERATE_MODEL)
    @Override
    public DeviceStatusDataResponse getCurrentAppStatus(int appId) {
        ArrayList<String> deviceNames = new ArrayList<>();
        DeviceStatusDataResponse response = new DeviceStatusDataResponse();
        List<AppDeviceDTO> appDevices = appDeviceMapper.getAppDevice(appId);
        response.setDeviceNum(appDevices.size());
        if (appDevices.size() != 0){
            for (AppDeviceDTO appDevice : appDevices) {
                deviceNames.add(appDevice.getDeviceName());
            }
            Long appDistributionNum = appStatusMapper.currentAppDistributionNum(deviceNames);
            Long appOnlineKeyNum = appStatusMapper.currentAppOnlineKeyNum(deviceNames);
            response.setKeyDistributionNum(appDistributionNum);
            response.setOnlineKeyNum(appOnlineKeyNum);
            DeviceStatusDataInfo deviceStatusInfo = appStatusMapper.getDeviceStatusInfo(deviceNames);
            response.setOnlineNum(deviceStatusInfo.getOnlineNum())
                    .setOfflineNum(deviceStatusInfo.getDeviceNum() - deviceStatusInfo.getOnlineNum())
                    .setEncDataNum(deviceStatusInfo.getEncDataNum())
                    .setDecDataNum(deviceStatusInfo.getDecDataNum());
            return response;
        }
        return response;
    }

    @OperateLogAnno(operateDesc = "查看特定应用绑定终端状态数据", operateModel = OPERATE_MODEL)
    @Override
    public List<DeviceStatus> listDeviceStatusInfo(int appId) {
        ArrayList<String> deviceNames = new ArrayList<>();
        List<DeviceStatus> deviceStatuses = new ArrayList<>();
        List<AppDeviceDTO> appDevices = appDeviceMapper.getAppDevice(appId);
        if (appDevices.size() != 0) {
            for (AppDeviceDTO appDevice : appDevices) {
                deviceNames.add(appDevice.getDeviceName());
            }
        }
        if (deviceNames.size()!=0)
            deviceStatuses = appStatusMapper.listDeviceStatusInfo(deviceNames);
        return deviceStatuses;
    }
}
