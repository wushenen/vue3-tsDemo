package com.cucc.unicom.pojo;

import lombok.Data;

@Data
public class MailConfigInfo {
    private String emailHost;
    private String emailUsername;
    private String emailPassword;
    private Integer emailPort;
    private String emailProtocol;
    private String defaultEncoding="UTF-8";
}
