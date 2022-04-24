package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.DTO.DeviceStatusDataInfo;
import com.unicom.quantum.pojo.DeviceStatus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppStatusMapper {
    Long currentAppDistributionNum(List<String> deviceNames);
    Long currentAppOnlineKeyNum(List<String> deviceNames);
    List<DeviceStatus> listDeviceStatusInfo(int appId);
    DeviceStatusDataInfo getDeviceStatusInfo(List<String> deviceNames);
}
