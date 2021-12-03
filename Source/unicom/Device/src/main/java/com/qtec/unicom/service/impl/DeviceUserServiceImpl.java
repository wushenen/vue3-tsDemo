package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.component.util.HexUtils;
import com.qtec.unicom.component.util.UtilService;
import com.qtec.unicom.controller.vo.UpdateUserInfoRequest;
import com.qtec.unicom.mapper.DeviceUserMapper;
import com.qtec.unicom.pojo.DTO.ExportDeviceUserInfo;
import com.qtec.unicom.pojo.DeviceUser;
import com.qtec.unicom.service.DeviceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceUserServiceImpl implements DeviceUserService {

    private final String OPERATE_MODEL = "终端用户模块";

    @Autowired
    private DeviceUserMapper deviceUserMapper;

    @Autowired
    private UtilService utilService;

    @OperateLogAnno(operateDesc = "获取所有终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public List<DeviceUser> listAllDeviceUser(String deviceName) {
        List<DeviceUser> deviceUsers = deviceUserMapper.listAllDeviceUser(deviceName);
        for (DeviceUser deviceUser : deviceUsers) {
            deviceUser.setPassword("***");
            deviceUser.setEncKey("***".getBytes());
        }
        return deviceUsers;
    }

    @OperateLogAnno(operateDesc = "获取特定终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public DeviceUser getDeviceInfo(int deviceId) {
        DeviceUser deviceInfo = deviceUserMapper.getDeviceInfo(deviceId);
        deviceInfo.setPassword(utilService.decryptCBC(deviceInfo.getPassword(),UtilService.SM4KEY));
        deviceInfo.setEncKey(utilService.decryptCBC(deviceInfo.getEncKey(),UtilService.SM4KEY));
        return deviceInfo;
    }

    @Override
    public boolean userNameIsExist(String deviceName) {
        return deviceUserMapper.userNameIsExist(deviceName);
    }

    @OperateLogAnno(operateDesc = "添加终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public int addDeviceUser(DeviceUser deviceUser) {
        deviceUser.setPassword(utilService.encryptCBC(deviceUser.getPassword(),UtilService.SM4KEY));
        if (deviceUser.getEncKey() != null) {
            deviceUser.setEncKey(utilService.encryptCBC(deviceUser.getEncKey(),UtilService.SM4KEY));
        }
        return deviceUserMapper.addDeviceUser(deviceUser);
    }

    @OperateLogAnno(operateDesc = "修改终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public int updateDevice(UpdateUserInfoRequest updateUserInfoRequest) {
        updateUserInfoRequest.setPassword(utilService.encryptCBC(updateUserInfoRequest.getPassword(),UtilService.SM4KEY));
        return deviceUserMapper.updateDevice(updateUserInfoRequest);
    }

    @OperateLogAnno(operateDesc = "删除特定终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public int deleteDevice(int deviceId) {
        deviceUserMapper.deleteDeviceAuthByDeviceId(deviceId);
        deviceUserMapper.deleteRoleUserByDeviceId(deviceId);
        deviceUserMapper.deleteGroupUserByDeviceId(deviceId);
        deviceUserMapper.deleteStrategyAuthByDeviceId(deviceId);
        return deviceUserMapper.deleteDevice(deviceId);
    }

    @OperateLogAnno(operateDesc = "模糊查询终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public List<DeviceUser> queryDeviceUser(String deviceName) {
        return deviceUserMapper.queryDeviceUser(deviceName);
    }

    @Override
    public List<ExportDeviceUserInfo> exportDeviceUserInfo(List<Integer> deviceIds) {
        List<ExportDeviceUserInfo> userInfos = deviceUserMapper.exportDeviceUserInfo(deviceIds);
        for (ExportDeviceUserInfo userInfo : userInfos) {
            userInfo.setPassword(utilService.decryptCBC(userInfo.getPassword(),UtilService.SM4KEY));
            if (userInfo.getEncKey() != null) {
                userInfo.setEncKey(HexUtils.bytesToHexString(utilService.decryptCBC(HexUtils.hexStringToBytes(userInfo.getEncKey()),UtilService.SM4KEY)));
            }
        }
        return userInfos;
    }

    @Override
    public String getEncKey(String deviceName) {
        return HexUtils.bytesToHexString(utilService.decryptCBC(HexUtils.hexStringToBytes(deviceUserMapper.getEncKey(deviceName)),UtilService.SM4KEY));
//        return utilService.decryptCBC(deviceUserMapper.getEncKey(deviceName),UtilService.SM4KEY);
    }
}
