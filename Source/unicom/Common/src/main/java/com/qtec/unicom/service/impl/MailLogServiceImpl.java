package com.qtec.unicom.service.impl;

import com.qtec.unicom.mapper.MailLogMapper;
import com.qtec.unicom.pojo.MailLog;
import com.qtec.unicom.service.MailLogService;
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
