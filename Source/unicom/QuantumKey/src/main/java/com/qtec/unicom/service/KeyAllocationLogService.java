package com.qtec.unicom.service;

import com.qtec.unicom.pojo.KeyAllocationLog;

import java.util.List;

public interface KeyAllocationLogService {
    void addAllocationLog(KeyAllocationLog keyAllocationLog);
    KeyAllocationLog getAllocationLog(String assignedName);
}
