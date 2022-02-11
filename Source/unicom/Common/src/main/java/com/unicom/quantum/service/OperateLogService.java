package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.OperateLogRequest;
import com.unicom.quantum.pojo.OperateLog;

import java.util.List;


public interface OperateLogService {
    void insertOperateLog(OperateLog log);
    List<OperateLog> getOperateLogs(OperateLogRequest operateLogRequest);
    List<String> getOperator();
    List<String> getOperateModel(String operator);
    List<String> getOperateDetail(String operator, String operateModel);
}
