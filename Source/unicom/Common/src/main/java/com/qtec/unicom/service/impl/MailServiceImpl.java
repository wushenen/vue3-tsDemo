package com.qtec.unicom.service.impl;

import com.qtec.unicom.component.MailProperties;
import com.qtec.unicom.config.MailSenderConfig;
import com.qtec.unicom.mapper.MailLogMapper;
import com.qtec.unicom.mapper.MailMapper;
import com.qtec.unicom.pojo.MailInfo;
import com.qtec.unicom.pojo.MailLog;
import com.qtec.unicom.pojo.MailConfigInfo;
import com.qtec.unicom.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private MailMapper mailMapper;

    @Autowired
    MailSenderConfig mailSenderConfig;

    @Autowired
    MailProperties mailProperties;

    @Autowired
    private MailLogMapper mailLogMapper;


    @Override
    public MailConfigInfo getMailSenderConfig() {
        MailConfigInfo mailConfigInfo = mailMapper.getMailSenderConfig();
        return mailConfigInfo;
    }

    @Override
    public void updateMailSenderConfig(MailConfigInfo mailConfigInfo) {
        mailMapper.updateMailSenderConfig(mailConfigInfo);
    }


    public void sendSimpleMail(MailInfo mailInfo) {
        mailSenderConfig.clear();
        JavaMailSenderImpl sender = mailSenderConfig.getSender();
        System.out.println("sender.getUsername() = " + sender.getUsername());
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setFrom(sender.getUsername());
            message.setTo(mailInfo.getDestination());
            message.setSubject(mailInfo.getSubject());
            message.setSentDate(new Date());
            message.setText(mailInfo.getText());
            CompletableFuture.runAsync(()->{
                sender.send(message);
                mailLogMapper.insertMailLog(new MailLog(mailInfo.getDestination(),mailInfo.getSubject(),true));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
