package com.cucc.unicom.service;

import com.cucc.unicom.pojo.MailLog;

import java.util.List;

public interface MailLogService {

    int insertMailLog(MailLog mailLog);

    List<MailLog> getMailLogs();
}
