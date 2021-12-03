package com.qtec.unicom.service;

import com.qtec.unicom.controller.vo.UpdateQemsConfigRequest;
import com.qtec.unicom.pojo.DeviceOperation;
import com.qtec.unicom.pojo.QemsConfig;

public interface QemsConfigService {

    QemsConfig getQemsConfig();

    int updateQemsConfig(UpdateQemsConfigRequest updateQemsConfigRequest);

    int addQemsOperation(String deviceName, int operation);

    DeviceOperation getOperation(String deviceName);

    int delQemsOperation(String deviceName);


}
