package com.qtec.unicom.config;

import com.qtec.unicom.component.MailProperties;
import com.qtec.unicom.mapper.MailMapper;
import com.qtec.unicom.pojo.MailConfigInfo;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.Random;

@Component
@AllArgsConstructor
public class MailSenderConfig {

    private final List<JavaMailSenderImpl> senderList;

    private final MailProperties mailProperties;

    private final MailMapper mailMapper;


    public void buildMailSender() {
        MailConfigInfo mail = mailMapper.getMailSenderConfig();
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties properties = new Properties();
        properties.put("mail.smtp.socketFactory.class","javax.bet.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.ssl.enable","true");
        if (mail !=null){
            javaMailSender.setDefaultEncoding(mail.getDefaultEncoding());
            javaMailSender.setHost(mail.getEmailHost());
            javaMailSender.setPort(mail.getEmailPort());
            javaMailSender.setProtocol(mail.getEmailProtocol());
            javaMailSender.setUsername(mail.getEmailUsername());
            javaMailSender.setPassword(mail.getEmailPassword());
            javaMailSender.setJavaMailProperties(properties);
            properties.put("mail.smtp.socketFactory.port",mail.getEmailHost());
            // 添加数据
            senderList.add(javaMailSender);
        }
        else{
            javaMailSender.setDefaultEncoding(mailProperties.getDefaultEncoding());
            javaMailSender.setHost(mailProperties.getHost());
            javaMailSender.setPort(mailProperties.getPort());
            javaMailSender.setProtocol(mailProperties.getProtocol());
            javaMailSender.setUsername(mailProperties.getUsername());
            javaMailSender.setPassword(mailProperties.getPassword());
            properties.put("mail.smtp.socketFactory.port","465");
            // 添加数据
            senderList.add(javaMailSender);
        }

    }


    public JavaMailSenderImpl getSender() {
        if (senderList.isEmpty()) {
            buildMailSender();
        }
        return senderList.get(0);
    }

    public void clear() {
        senderList.clear();
    }

}
