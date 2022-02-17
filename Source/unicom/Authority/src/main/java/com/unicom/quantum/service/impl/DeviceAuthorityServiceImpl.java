package com.unicom.quantum.service.impl;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.component.ResultHelper;
import com.unicom.quantum.component.annotation.OperateLogAnno;
import com.unicom.quantum.mapper.DeviceAuthorityMapper;
import com.unicom.quantum.pojo.DTO.AuthInfo;
import com.unicom.quantum.service.DeviceAuthorityService;
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
    public int addDeviceAuthority(int deviceId, List<Integer> apiIds) throws QuantumException {
        for (Integer apiId : apiIds) {
            if (deviceAuthorityMapper.deviceAuthorityIsExist(deviceId,apiId)) {
                throw new QuantumException(ResultHelper.genResult(1,deviceAuthorityMapper.getApiName(apiId) +"  权限重复添加"));
            }
        }
        for (Integer apiId : apiIds) {
            deviceAuthorityMapper.addDeviceAuthority(deviceId, apiId);
        }
        return 0;
    }


    @OperateLogAnno(operateDesc = "删除终端权限", operateModel = OPERATE_MODEL)
    @Override
    public int delDeviceAuthority(int authId){
        deviceAuthorityMapper.delDeviceAuthority(authId);
        return 0;
    }

    @OperateLogAnno(operateDesc = "删除指定终端所有权限", operateModel = OPERATE_MODEL)
    @Override
    public int delDeviceAuthByDeviceId(int deviceId) {
        deviceAuthorityMapper.delDeviceAuthByDeviceId(deviceId);
        return 0;
    }

    @OperateLogAnno(operateDesc = "获取指定终端权限", operateModel = OPERATE_MODEL)
    @Override
    public List<AuthInfo> getDeviceAuthority(int deviceId) {
        return deviceAuthorityMapper.getDeviceAuthority(deviceId);
    }
}
