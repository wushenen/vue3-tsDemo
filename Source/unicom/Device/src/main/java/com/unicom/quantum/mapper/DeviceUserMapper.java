package com.unicom.quantum.mapper;

import com.unicom.quantum.pojo.DTO.ExportDeviceUserInfo;
import com.unicom.quantum.pojo.DeviceUser;
import com.unicom.quantum.controller.vo.UpdateUserInfoRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeviceUserMapper {

    int addDeviceUser(DeviceUser deviceUser);
    boolean userNameIsExist(@Param("deviceName") String deviceName);
    int updateDevice(UpdateUserInfoRequest updateUserInfoRequest);
    int deleteDevice(@Param("deviceId") int deviceId);
    int deleteGroupUserByDeviceId(@Param("deviceId") int deviceId);
    int deleteDeviceAuthByDeviceId(@Param("deviceId") int deviceId);
    int deleteKeyInfoByDeviceName(String deviceName);
    int deleteKeyLimitByDeviceName(String deviceName);
    int deleteAppDevice(@Param("deviceId") int deviceId);
    int deleteDeviceStatus(@Param("deviceName") String deviceName);
    List<DeviceUser> listAllDeviceUser(@Param("deviceName") String deviceName);
    DeviceUser getDeviceInfo(@Param("deviceId") int deviceId);
    List<DeviceUser> queryDeviceUser(@Param("deviceName") String deviceName);
    List<ExportDeviceUserInfo> exportDeviceUserInfo(@Param("deviceIds") List<Integer> deviceIds);
    String getEncKey(@Param("deviceName") String deviceName);
}
