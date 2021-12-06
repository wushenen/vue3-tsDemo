package com.qtec.unicom.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeyLimit implements Serializable {
    private int id;
    private String userName;
    private int userType;
    private int limitNum;
}
