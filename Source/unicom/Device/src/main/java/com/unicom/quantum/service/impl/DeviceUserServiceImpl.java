package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.DataTools;
import com.unicom.quantum.component.util.HexUtils;
import com.unicom.quantum.component.util.UtilService;
import com.unicom.quantum.controller.vo.UpdateUserInfoRequest;
import com.unicom.quantum.mapper.DeviceUserMapper;
import com.unicom.quantum.pojo.DTO.ExportDeviceUserInfo;
import com.unicom.quantum.pojo.DeviceUser;
import com.unicom.quantum.service.DeviceUserService;
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

    @OperateLogAnno(operateDesc = "查看所有终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public List<DeviceUser> listAllDeviceUser(String deviceName) {
        List<DeviceUser> deviceUsers = deviceUserMapper.listAllDeviceUser(deviceName);
        for (DeviceUser deviceUser : deviceUsers) {
            deviceUser.setPassword("***");
            deviceUser.setEncKey("***".getBytes());
        }
        return deviceUsers;
    }

    @OperateLogAnno(operateDesc = "查看特定终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public DeviceUser getDeviceInfo(int deviceId) {
        DeviceUser deviceInfo = deviceUserMapper.getDeviceInfo(deviceId);
        deviceInfo.setPassword(utilService.decryptCBCWithPadding(deviceInfo.getPassword(),UtilService.SM4KEY));
        deviceInfo.setEncKey(utilService.decryptCBCWithPadding(deviceInfo.getEncKey(),UtilService.SM4KEY));
        return deviceInfo;
    }

    @Override
    public boolean userNameIsExist(String deviceName) {
        return deviceUserMapper.userNameIsExist(deviceName);
    }

    @OperateLogAnno(operateDesc = "添加终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public int addDeviceUser(DeviceUser deviceUser) throws Exception {
        if (deviceUserMapper.userNameIsExist(deviceUser.getDeviceName())) {
            throw new QuantumException(ResultHelper.genResult(1,"用户名已被占用"));
        }
        deviceUser.setPassword(DataTools.encryptMessage(deviceUser.getPassword()));
        if (deviceUser.getUserType() == 1){
            deviceUser.setEncKey(DataTools.encryptMessage(utilService.generateQuantumRandom(32)));
        }
        deviceUserMapper.addDeviceUser(deviceUser);
        return 0;
    }

    @OperateLogAnno(operateDesc = "修改终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public int updateDevice(UpdateUserInfoRequest updateUserInfoRequest) throws Exception {
        String oldDeviceName = deviceUserMapper.getDeviceInfo(updateUserInfoRequest.getDeviceId()).getDeviceName();
        if (!oldDeviceName.equals(updateUserInfoRequest.getDeviceName()) && deviceUserMapper.userNameIsExist(updateUserInfoRequest.getDeviceName())) {
            throw new QuantumException(ResultHelper.genResult(1,"用户名已被占用"));
        }
        if (updateUserInfoRequest.getPassword()!=null) {
            updateUserInfoRequest.setPassword(DataTools.encryptMessage(updateUserInfoRequest.getPassword()));
        }
        return deviceUserMapper.updateDevice(updateUserInfoRequest);
    }

    @OperateLogAnno(operateDesc = "删除特定终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public int deleteDevice(int deviceId) {
        deviceUserMapper.deleteDeviceAuthByDeviceId(deviceId);
        deviceUserMapper.deleteGroupUserByDeviceId(deviceId);
        DeviceUser deviceInfo = deviceUserMapper.getDeviceInfo(deviceId);
        deviceUserMapper.deleteKeyInfoByDeviceName(deviceInfo.getDeviceName());
        deviceUserMapper.deleteKeyLimitByDeviceName(deviceInfo.getDeviceName());
        deviceUserMapper.deleteAppDevice(deviceId);
        deviceUserMapper.deleteDeviceStatus(deviceInfo.getDeviceName());
        deviceUserMapper.deleteDevice(deviceId);
        return 0;
    }

    @OperateLogAnno(operateDesc = "查看终端用户信息", operateModel = OPERATE_MODEL)
    @Override
    public List<DeviceUser> queryDeviceUser(String deviceName) {
        return deviceUserMapper.queryDeviceUser(deviceName);
    }

    @Override
    public List<ExportDeviceUserInfo> exportDeviceUserInfo(List<Integer> deviceIds) throws Exception {
        List<ExportDeviceUserInfo> userInfos = deviceUserMapper.exportDeviceUserInfo(deviceIds);
        for (ExportDeviceUserInfo userInfo : userInfos) {
            userInfo.setPassword(DataTools.decryptMessage(userInfo.getPassword()));
            if (userInfo.getEncKey() != null) {
                userInfo.setEncKey(HexUtils.bytesToHexString(DataTools.decryptMessage(HexUtils.hexStringToBytes(userInfo.getEncKey()))));
            }
        }
        return userInfos;
    }

    @Override
    public String getEncKey(String deviceName) throws Exception {
        String encKey = HexUtils.bytesToHexString(DataTools.decryptMessage(HexUtils.hexStringToBytes(deviceUserMapper.getEncKey(deviceName))));
        if (encKey != null && !encKey.equals(""))
            return encKey;
        else
            throw new QuantumException(ResultHelper.genResult(1,"用户加密密钥错误"));
    }
}
