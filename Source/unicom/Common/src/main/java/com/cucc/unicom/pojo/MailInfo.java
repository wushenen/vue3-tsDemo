package com.cucc.unicom.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailInfo {

    private String subject;

    private String destination;

    private String text;

    public MailInfo(String subject, String destination, String text) {
        this.subject = subject;
        this.destination = destination;
        this.text = text;
    }
}
