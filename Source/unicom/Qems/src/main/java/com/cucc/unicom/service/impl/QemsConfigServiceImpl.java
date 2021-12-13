package com.cucc.unicom.service.impl;

import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.mapper.QemsConfigMapper;
import com.cucc.unicom.pojo.DeviceOperation;
import com.cucc.unicom.pojo.QemsConfig;
import com.cucc.unicom.service.QemsConfigService;
import com.cucc.unicom.controller.vo.UpdateQemsConfigRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QemsConfigServiceImpl implements QemsConfigService {

    private final String OPERATE_MODEL = "QEMS模块";

    @Autowired
    private QemsConfigMapper qemsConfigMapper;

    @Override
    public QemsConfig getQemsConfig() {
        return qemsConfigMapper.getQemsConfig();
    }

    @OperateLogAnno(operateDesc = "修改QEMS配置", operateModel = OPERATE_MODEL)
    @Override
    public int updateQemsConfig(UpdateQemsConfigRequest updateQemsConfigRequest) {
        //如果没有改动则不用更新时间，后续可以作为优化点
        qemsConfigMapper.updateQemsConfig(updateQemsConfigRequest);
        return 0;
    }

    @OperateLogAnno(operateDesc = "设备指令控制", operateModel = OPERATE_MODEL)
    @Override
    public int addQemsOperation(String deviceName, int operation) {
        if (qemsConfigMapper.countQemsOperation(deviceName) != 0)
            return 1;
        qemsConfigMapper.addQemsOperation(deviceName, operation);
        return 0;
    }

    @Override
    public DeviceOperation getOperation(String deviceName) {
        return qemsConfigMapper.getOperation(deviceName);
    }

    @Override
    public int delQemsOperation(String deviceName) {
        return qemsConfigMapper.delQemsOperation(deviceName);
    }
}
