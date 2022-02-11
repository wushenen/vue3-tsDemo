package com.unicom.quantum.service.impl;

import com.unicom.quantum.mapper.MailLogMapper;
import com.unicom.quantum.pojo.MailLog;
import com.unicom.quantum.service.MailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailLogServiceImpl implements MailLogService {

    @Autowired
    private MailLogMapper mailLogMapper;

    @Override
    public int insertMailLog(MailLog mailLog) {
        return mailLogMapper.insertMailLog(mailLog);
    }

    @Override
    public List<MailLog> getMailLogs() {
        return mailLogMapper.getMailLogs();
    }
}
