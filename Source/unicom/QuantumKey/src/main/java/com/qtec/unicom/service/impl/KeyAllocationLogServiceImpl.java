package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.KeyAllocationLogMapper;
import com.qtec.unicom.pojo.KeyAllocationLog;
import com.qtec.unicom.service.KeyAllocationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyAllocationLogServiceImpl implements KeyAllocationLogService {

    @Autowired
    private KeyAllocationLogMapper keyAllocationLogMapper;

    @Override
    public void addAllocationLog(KeyAllocationLog keyAllocationLog) {
        keyAllocationLogMapper.addAllocationLog(keyAllocationLog);
    }

    @Override
    public KeyAllocationLog getAllocationLog(String assignedName) {
        List<KeyAllocationLog> allocationLog = keyAllocationLogMapper.getAllocationLog(assignedName);
        if (allocationLog.size() == 0)
            return null;
        return allocationLog.get(0);
    }
}
