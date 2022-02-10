package com.cucc.unicom.service;

import com.cucc.unicom.controller.vo.OperateLogRequest;
import com.cucc.unicom.pojo.OperateLog;

import java.util.List;


public interface OperateLogService {
    void insertOperateLog(OperateLog log);
    List<OperateLog> getOperateLogs(OperateLogRequest operateLogRequest);
}
