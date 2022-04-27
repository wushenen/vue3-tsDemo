package com.unicom.quantum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.component.util.CharacterUtil;
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
import org.springframework.web.multipart.MultipartFile;

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
    public DeviceUser getDeviceInfo(int deviceId) throws Exception {
        DeviceUser deviceInfo = deviceUserMapper.getDeviceInfo(deviceId);
        deviceInfo.setPassword(DataTools.decryptMessage(deviceInfo.getPassword()));
        deviceInfo.setEncKey(DataTools.decryptMessage(deviceInfo.getEncKey()));
        return deviceInfo;
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

    @Override
    public void importDeviceUser(MultipartFile fileData) throws Exception {
        String suffix = fileData.getOriginalFilename().substring(fileData.getOriginalFilename().lastIndexOf('.'));
        if (! ".xls".equals(suffix) && !".xlsx".equals(suffix)) {
            throw new QuantumException(ResultHelper.genResult(1,"模板文件格式有误"));
        }
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        List<DeviceUser> list = null;
        try {
            list = ExcelImportUtil.importExcel(fileData.getInputStream(), DeviceUser.class, importParams);
        } catch (Exception e) {
            throw new QuantumException(ResultHelper.genResult(1,"请确认导入模板是否正确！"));
        }
        for (DeviceUser insertUser : list) {
            if (insertUser.getDeviceName() == null || insertUser.getPassword() == null)
                throw new QuantumException(ResultHelper.genResult(1,"导入数据有误，请确认导入模板是否正确！"));
            if (CharacterUtil.containsChinese(insertUser.getDeviceName()))
                throw new QuantumException(ResultHelper.genResult(1,"终端用户名不支持中文字符，请检查后再导入！"));
            if(insertUser.getDeviceName().trim() == null && insertUser.getPassword().trim() == null && insertUser.getComments().trim() == null)
                throw new QuantumException(ResultHelper.genResult(1,"导入数据中至少有一条数据的用户名、密码和备注为空"));
            if (insertUser.getDeviceName().length() < 4 || insertUser.getDeviceName().length() > 16)
                throw new QuantumException(ResultHelper.genResult(1,"终端用户名："+insertUser.getDeviceName()+"不符合长度限制(4-16位)"));
            if (insertUser.getPassword().length() < 8 || insertUser.getPassword().length() > 16)
                throw new QuantumException(ResultHelper.genResult(1,"终端用户："+insertUser.getDeviceName()+"密码不符合长度限制(8-16位)"));
        }
        for (DeviceUser deviceUser : list) {
            String userName = deviceUser.getDeviceName();
            if (deviceUserMapper.userNameIsExist(userName))
                throw new QuantumException(ResultHelper.genResult(1,"终端用户：" + userName + "已存在，数据导入失败"));
        }
        for (DeviceUser deviceUser : list) {
            deviceUser.setPassword(DataTools.encryptMessage(deviceUser.getPassword()));
            if (deviceUser.getUserType() == 1) {
                deviceUser.setEncKey(DataTools.encryptMessage(utilService.generateQuantumRandom(32)));
            }
            if (!deviceUserMapper.userNameIsExist(deviceUser.getDeviceName()))
                deviceUserMapper.addDeviceUser(deviceUser);
        }
    }
}
