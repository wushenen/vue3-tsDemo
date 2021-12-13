/*
package com.qtec.unicom.component.util;

import com.qtec.unicom.pojo.MailInfo;
import com.qtec.unicom.pojo.MailLog;
import com.qtec.unicom.service.MailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Component
public class MailService {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailLogService mailLogService;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleMail(MailInfo mailInfo) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setFrom(sender);
            message.setTo(mailInfo.getDestination());
            message.setSubject(mailInfo.getSubject());
            message.setSentDate(new Date());
            message.setText(mailInfo.getText());
            threadPool.execute(()->{
                javaMailSender.send(message);
            });
            mailLogService.insertMailLog(new MailLog(mailInfo.getDestination(),mailInfo.getSubject(),true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
