package com.qtec.unicom.config;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartListener implements ApplicationListener<ApplicationStartedEvent> {

    MailSenderConfig mailSenderConfig;

    public StartListener(MailSenderConfig mailSenderConfig){
        this.mailSenderConfig = mailSenderConfig;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        this.mailSenderConfig.buildMailSender();
    }
}
