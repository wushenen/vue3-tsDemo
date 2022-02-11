package com.unicom.quantum.service;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.pojo.DTO.ExportDeviceUserInfo;
import com.unicom.quantum.pojo.DeviceUser;
import com.unicom.quantum.controller.vo.UpdateUserInfoRequest;

import java.util.List;

public interface DeviceUserService {

    /*获取所有终端用户信息*/
    List<DeviceUser> listAllDeviceUser(String deviceName);

    /*获取终端用户信息*/
    DeviceUser getDeviceInfo(int deviceId);

    /*判断的用户名是否已被占用*/
    boolean userNameIsExist(String deviceName);

    /*添加终端用户信息*/
    int addDeviceUser(DeviceUser deviceUser) throws Exception;

    /*修改终端用户信息*/
    int updateDevice(UpdateUserInfoRequest updateUserInfoRequest) throws QuantumException;

    /*删除终端用户信息*/
    int deleteDevice(int deviceId);

    /*模糊查询终端用户信息*/
    List<DeviceUser> queryDeviceUser(String deviceName);

    /*导出指定用户*/
    List<ExportDeviceUserInfo> exportDeviceUserInfo(List<Integer> deviceIds);

    /*获取终端用户加密密钥*/
    String getEncKey(String deviceName);

}
