package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.MailLogMapper;
import com.cucc.unicom.pojo.MailLog;
import com.cucc.unicom.service.MailLogService;
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
