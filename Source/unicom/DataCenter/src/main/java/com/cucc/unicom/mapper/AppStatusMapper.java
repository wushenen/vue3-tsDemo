package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.DTO.DeviceStatusDataInfo;
import com.cucc.unicom.pojo.DeviceStatus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppStatusMapper {
    Long currentAppDistributionNum(List<String> deviceNames);
    Long currentAppOnlineKeyNum(List<String> deviceNames);
    List<DeviceStatus> listDeviceStatusInfo(List<String> deviceNames);
    DeviceStatusDataInfo getDeviceStatusInfo(List<String> deviceNames);
}
