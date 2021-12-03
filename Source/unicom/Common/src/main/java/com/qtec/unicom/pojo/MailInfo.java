package com.qtec.unicom.pojo;


public class MailInfo {

    private String subject;

    private String destination;

    private String text;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MailInfo() {
    }

    public MailInfo(String subject, String destination, String text) {
        this.subject = subject;
        this.destination = destination;
        this.text = text;
    }
}
