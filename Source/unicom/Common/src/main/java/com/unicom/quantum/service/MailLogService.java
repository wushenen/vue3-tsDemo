package com.unicom.quantum.service;

import com.unicom.quantum.pojo.MailLog;

import java.util.List;

public interface MailLogService {

    int insertMailLog(MailLog mailLog);

    List<MailLog> getMailLogs();
}
