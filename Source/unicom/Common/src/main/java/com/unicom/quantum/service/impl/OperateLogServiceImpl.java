package com.unicom.quantum.service.impl;

import com.unicom.quantum.controller.vo.OperateLogRequest;
import com.unicom.quantum.mapper.OperateLogMapper;
import com.unicom.quantum.pojo.OperateLog;
import com.unicom.quantum.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public void insertOperateLog(OperateLog log) {
        operateLogMapper.insertOperateLog(log);
    }

	@Override
	public List<OperateLog> getOperateLogs(OperateLogRequest operateLogRequest) {
        return operateLogMapper.getOperateLogs(operateLogRequest);
    }

    @Override
    public List<String> getOperator() {
        return operateLogMapper.getOperator();
    }

    @Override
    public List<String> getOperateModel(String operator) {
        return operateLogMapper.getOperateModel(operator);
    }

    @Override
    public List<String> getOperateDetail(String operator, String operateModel) {
        return operateLogMapper.getOperateDetail(operator,operateModel);
    }
}
