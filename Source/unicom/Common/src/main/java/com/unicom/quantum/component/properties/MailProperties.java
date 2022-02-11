package com.unicom.quantum.component.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
    private String username;
    private String password;
    private String host;
    private Integer port;
    private String protocol;
    private String defaultEncoding;
}


