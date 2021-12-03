package com.qtec.unicom.service.impl;

import com.qtec.unicom.controller.vo.DeviceStatusDataResponse;
import com.qtec.unicom.mapper.DeviceStatusMapper;
import com.qtec.unicom.pojo.DTO.DeviceKeyInfo;
import com.qtec.unicom.pojo.DTO.DeviceStatusDataInfo;
import com.qtec.unicom.pojo.DeviceStatus;
import com.qtec.unicom.service.DeviceStatusService;
import com.qtec.unicom.util.CalculateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {

    @Autowired
    private DeviceStatusMapper deviceStatusMapper;

    @Override
    public int updateDeviceStatusInfo(DeviceStatus deviceStatus) {
        deviceStatus.setEncRate(CalculateUtil.calRate(deviceStatus.getEncData()))
                .setDecRate(CalculateUtil.calRate(deviceStatus.getDecData()))
                .setWorkStatus(true)
                .setOnlineTime(new Date(System.currentTimeMillis()));
        DeviceStatus info = deviceStatusMapper.getDeviceStatusInfoByDeviceName(deviceStatus.getDeviceName());
        if (info != null) {
            deviceStatus.setKeyNum(info.getKeyNum() + deviceStatus.getKeyNum())
                    .setEncData(info.getEncData() + deviceStatus.getEncData())
                    .setDecData(info.getDecData() + deviceStatus.getDecData());
            //判断原来的状态是否在线
            if (info.isWorkStatus())
                deviceStatus.setOnlineTime(info.getOnlineTime());
            deviceStatusMapper.updateDeviceStatusInfo(deviceStatus);
            return 0;
        }
        deviceStatusMapper.addDeviceStatusInfo(deviceStatus);
        return 0;
    }

    @Override
    public int addDeviceStatusInfo(DeviceStatus deviceStatus) {
        if (deviceStatusMapper.deviceStatusInfoExist(deviceStatus.getDeviceName())) {
            return 1;
        }
        return deviceStatusMapper.addDeviceStatusInfo(deviceStatus);
    }

    @Override
    public List<DeviceStatus> listDeviceStatusInfo() {
        return deviceStatusMapper.listDeviceStatusInfo();
    }

    @Override
    public DeviceStatusDataResponse getStatusShowInfo() {
        DeviceStatusDataResponse response = new DeviceStatusDataResponse();
        //数据总量
        DeviceStatusDataInfo info = deviceStatusMapper.getDeviceStatusInfo();
        Long onlineKeyNum = deviceStatusMapper.keyGenNum();
        //密钥信息获取
        List<DeviceKeyInfo> everyDayKeyInfos = deviceStatusMapper.everyDayKeyInfo();
        Long nearlyDayKeyNum = deviceStatusMapper.nearlyDayKeyNum();
        Long offlineKeyNum = deviceStatusMapper.offlineKeyNum();
        response.setDeviceNum(info.getDeviceNum())
                .setOnlineNum(info.getOnlineNum())
                .setOfflineNum(info.getDeviceNum() - info.getOnlineNum())
                .setKeyUseNum(info.getKeyNum())
                .setEncDataNum(info.getEncDataNum())
                .setDecDataNum(info.getDecDataNum())
                .setOnlineKeyNum(onlineKeyNum)
                .setEveryDayKeyNum(everyDayKeyInfos)
                .setOneDayKeyNum(nearlyDayKeyNum)
                .setOfflineKeyNum(offlineKeyNum)
                .setKeyGenNum(onlineKeyNum+offlineKeyNum);


        return response;
    }

    /**
     * 定时监测设备存活或更新操作数据
     * operation=0为定时任务；1-重启；2-置零
     * @param deviceName
     * @param operation
     */
    @Override
    public void checkDeviceStatus(String deviceName,int operation) {
        switch (operation){
            case 0:
                List<DeviceStatus> deviceStatuses = deviceStatusMapper.listDeviceStatusInfo();
                for (DeviceStatus deviceStatus : deviceStatuses) {
                    if (deviceStatus.isWorkStatus()) {
                        if (System.currentTimeMillis() - deviceStatus.getUpdateTime().getTime() > 60000) {
                            deviceStatus.setWorkStatus(false);
                            deviceStatus.setOnlineTime(null);
                            deviceStatusMapper.updateDeviceStatusInfo(deviceStatus);
                        }
                    }
                }
                break;
            case 1:
                DeviceStatus deviceStatus = deviceStatusMapper.getDeviceStatusInfoByDeviceName(deviceName);
                deviceStatus.setDeviceIp(null)
                        .setWorkStatus(false)
                        .setOnlineTime(null)
                        .setWorkTime(null)
                        .setDecRate(null)
                        .setEncRate(null);
                deviceStatusMapper.updateDeviceStatusInfo(deviceStatus);
                break;
            case 2:
                DeviceStatus deviceStatusInfo = new DeviceStatus();
                deviceStatusInfo.setDeviceName(deviceName)
                        .setWorkTime(null)
                        .setWorkStatus(false)
                        .setOnlineTime(null)
                        .setDecData(0L)
                        .setEncData(0L)
                        .setKeyNum(0L)
                        .setDecRate(null)
                        .setEncRate(null);
                deviceStatusMapper.updateDeviceStatusInfo(deviceStatusInfo);
                break;
            default:
        }

    }
}
