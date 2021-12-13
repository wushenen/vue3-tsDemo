package com.cucc.unicom.service;

import com.cucc.unicom.pojo.MailConfigInfo;
import com.cucc.unicom.pojo.MailInfo;

public interface MailService {

    MailConfigInfo getMailSenderConfig();

    void updateMailSenderConfig(MailConfigInfo mailConfigInfo);

    void sendSimpleMail(MailInfo mailInfo);
}
