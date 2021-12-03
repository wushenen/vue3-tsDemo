package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.annotation.OperateLogAnno;
import com.qtec.unicom.mapper.DeviceAuthorityMapper;
import com.qtec.unicom.pojo.DTO.AuthInfo;
import com.qtec.unicom.service.DeviceAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceAuthorityServiceImpl implements DeviceAuthorityService {

    private final String OPERATE_MODEL = "终端授权模块";

    @Autowired
    private DeviceAuthorityMapper deviceAuthorityMapper;

    @OperateLogAnno(operateDesc = "添加终端权限", operateModel = OPERATE_MODEL)
    @Override
    public int addDeviceAuthority(int deviceId, int apiId) {
        return deviceAuthorityMapper.addDeviceAuthority(deviceId, apiId);
    }

    @Override
    public boolean deviceAuthorityIsAdd(int deviceId, int apiId) {
        return deviceAuthorityMapper.deviceAuthorityIsAdd(deviceId, apiId);
    }

    @OperateLogAnno(operateDesc = "删除终端权限", operateModel = OPERATE_MODEL)
    @Override
    public int delDeviceAuthority(int authId) {
        return deviceAuthorityMapper.delDeviceAuthority(authId);
    }

    @OperateLogAnno(operateDesc = "删除指定终端所有权限", operateModel = OPERATE_MODEL)
    @Override
    public int delDeviceAuthByDeviceId(int deviceId) {
        return deviceAuthorityMapper.delDeviceAuthByDeviceId(deviceId);
    }

    @OperateLogAnno(operateDesc = "获取指定终端权限", operateModel = OPERATE_MODEL)
    @Override
    public List<AuthInfo> getDeviceAuthority(int deviceId) {
        return deviceAuthorityMapper.getDeviceAuthority(deviceId);
    }
}
