package com.qtec.unicom.mapper;

import com.qtec.unicom.pojo.DTO.DeviceKeyInfo;
import com.qtec.unicom.pojo.DTO.DeviceStatusDataInfo;
import com.qtec.unicom.pojo.DeviceStatus;
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



    //密码态势感知数据总量获取
    DeviceStatusDataInfo getDeviceStatusInfo();
    //近5天密码态势感知数据获取
    DeviceStatusDataInfo getDeviceStatusInfo2();
    //在线密钥生产总量
    Long keyGenNum();
    Long keyUsedNum();
    //近5天密钥生产总量
    Long keyGenNum2();

    //近5日每日密钥产生情况
    List<DeviceKeyInfo> everyDayKeyInfo();
    //最近24小时密钥生产数量
    Long nearlyDayKeyNum();

    Long offlineKeyNum();



}
