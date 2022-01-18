package com.cucc.unicom.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CurrentAppManager implements Serializable {

    private int userId;
    private String userName;

}
