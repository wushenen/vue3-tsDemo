package com.qtec.unicom.service;

import com.qtec.unicom.pojo.MailInfo;
import com.qtec.unicom.pojo.MailConfigInfo;

public interface MailService {

    MailConfigInfo getMailSenderConfig();

    void updateMailSenderConfig(MailConfigInfo mailConfigInfo);

    void sendSimpleMail(MailInfo mailInfo);
}
