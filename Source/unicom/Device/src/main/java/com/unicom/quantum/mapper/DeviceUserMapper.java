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

    /*添加终端用户信息*/
    int addDeviceUser(DeviceUser deviceUser);
    /*判断用户名是否已被占用*/
    boolean userNameIsExist(@Param("deviceName") String deviceName);

    /*修改终端用户信息*/
    int updateDevice(UpdateUserInfoRequest updateUserInfoRequest);

    /*删除终端用户信息*/
    int deleteDevice(@Param("deviceId") int deviceId);
    /*删除终端的同时删除权限表中关于该终端的信息*/
    int deleteGroupUserByDeviceId(@Param("deviceId") int deviceId);
    int deleteDeviceAuthByDeviceId(@Param("deviceId") int deviceId);
    //删除密钥信息
    int deleteKeyInfoByDeviceName(String deviceName);
    int deleteKeyLimitByDeviceName(String deviceName);
    //删除已绑定的应用信息
    int deleteAppDevice(@Param("deviceId") int deviceId);
    int deleteDeviceStatus(@Param("deviceName") String deviceName);


    /*获取所有终端用户信息*/
    List<DeviceUser> listAllDeviceUser(@Param("deviceName") String deviceName);

    /*获取终端用户信息*/
    DeviceUser getDeviceInfo(@Param("deviceId") int deviceId);

    /*模糊查询终端用户信息*/
    List<DeviceUser> queryDeviceUser(@Param("deviceName") String deviceName);


    /*导出指定用户*/
    List<ExportDeviceUserInfo> exportDeviceUserInfo(@Param("deviceIds") List<Integer> deviceIds);

    /*获取终端用户加密密钥*/
    String getEncKey(@Param("deviceName") String deviceName);
}
