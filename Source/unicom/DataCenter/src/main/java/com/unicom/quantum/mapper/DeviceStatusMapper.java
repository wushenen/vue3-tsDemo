package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.DTO.DeviceStatusDataInfo;
import com.unicom.quantum.pojo.DeviceStatus;
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
    Long keyDistributionNum();
    Long onlineKeyNum();
    Long onlineEnableKeyNum();



}
