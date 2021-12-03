package com.qtec.unicom.service;

import com.qtec.unicom.pojo.MailLog;

import java.util.List;

public interface MailLogService {

    int insertMailLog(MailLog mailLog);

    List<MailLog> getMailLogs();
}
