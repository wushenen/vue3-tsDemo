package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.dto.AppDeviceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppDeviceMapper {
    int addAppDevice(int appId, int deviceId);
    boolean appDeviceExist(int appId, int deviceId);
    int deleteAppDevice(int appId, int deviceId);
    List<AppDeviceDTO> getAppDevice(int appId);

    String getDeviceName(int deviceId);
    int addDeviceToDeviceStatus(String deviceName);
    boolean deviceStatusInfoExist(String deviceName);
    boolean deleteDeviceStatusInfo(List<String> deviceNames);

}
