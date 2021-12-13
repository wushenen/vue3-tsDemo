package com.cucc.unicom.service;

import com.cucc.unicom.pojo.DeviceOperation;
import com.cucc.unicom.pojo.QemsConfig;
import com.cucc.unicom.controller.vo.UpdateQemsConfigRequest;

public interface QemsConfigService {

    QemsConfig getQemsConfig();

    int updateQemsConfig(UpdateQemsConfigRequest updateQemsConfigRequest);

    int addQemsOperation(String deviceName, int operation);

    DeviceOperation getOperation(String deviceName);

    int delQemsOperation(String deviceName);


}
