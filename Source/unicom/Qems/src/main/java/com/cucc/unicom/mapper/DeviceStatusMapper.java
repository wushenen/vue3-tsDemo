package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.DTO.DeviceKeyInfo;
import com.cucc.unicom.pojo.DTO.DeviceStatusDataInfo;
import com.cucc.unicom.pojo.DeviceStatus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceStatusMapper {

    int addDeviceStatusInfo(DeviceStatus deviceStatus);
    boolean deviceStatusInfoExist(String deviceName);
    int updateDeviceStatusInfo(DeviceStatus deviceStatus);
    List<DeviceStatus> listDeviceStatusInfo();
    DeviceStatus getDeviceStatusInfoByDeviceName(String deviceName);
    DeviceStatusDataInfo getDeviceStatusInfo();
    Long keyGenNum();
    Long keyUsedNum();
    List<DeviceKeyInfo> everyDayKeyInfo();
    Long nearlyDayKeyNum();
    Long offlineKeyNum();



}
