package com.cucc.unicom.mapper;

import com.cucc.unicom.pojo.dto.AppDeviceDTO;
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
}
