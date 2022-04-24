package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.controller.vo.DeviceStatusDataResponse;
import com.unicom.quantum.mapper.AppStatusMapper;
import com.unicom.quantum.pojo.DTO.DeviceStatusDataInfo;
import com.unicom.quantum.pojo.DeviceStatus;
import com.unicom.quantum.service.AppStatusService;
import com.unicom.quantum.mapper.AppDeviceMapper;
import com.unicom.quantum.pojo.dto.AppDeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
        List<DeviceStatus> deviceStatuses = appStatusMapper.listDeviceStatusInfo(appId);
        for (DeviceStatus deviceStatus : deviceStatuses) {
            if (deviceStatus.getOnlineTime() != null) {
                long time = (new Date().getTime() - deviceStatus.getOnlineTime().getTime()) / 1000;
                long hours = time / 3600;
                long minutes = (time - (hours * 3600)) / 60;
                long seconds = time - (hours * 3600) - (minutes * 60);
                deviceStatus.setWorkTime(hours + "时" + minutes + "分" + seconds + "秒");
            } else {
                deviceStatus.setDeviceIp(null);
                deviceStatus.setWorkTime(null);
                deviceStatus.setEncRate(null);
                deviceStatus.setDecRate(null);
            }
        }
        return deviceStatuses;
    }
}
