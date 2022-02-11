package com.unicom.quantum.service;

import com.unicom.quantum.pojo.MailConfigInfo;
import com.unicom.quantum.pojo.MailInfo;

public interface MailService {

    MailConfigInfo getMailSenderConfig();

    void updateMailSenderConfig(MailConfigInfo mailConfigInfo);

    void sendSimpleMail(MailInfo mailInfo);
}
