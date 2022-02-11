package com.unicom.quantum.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MailLog {
    private int id;
    private String destination;
    private String detail;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private boolean mailStatus;

    public MailLog(String destination, String detail, boolean mailStatus) {
        this.destination = destination;
        this.detail = detail;
        this.mailStatus = mailStatus;
    }
}
