package com.qtec.unicom.pojo.DTO;

import lombok.Data;

import java.io.Serializable;
@Data
public class KeyOfflineDTO implements Serializable {
    private String keyId;
    private String keyValue;
}
