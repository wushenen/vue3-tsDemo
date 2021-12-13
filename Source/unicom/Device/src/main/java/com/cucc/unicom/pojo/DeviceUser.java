package com.cucc.unicom.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DeviceUser implements Serializable {

    @ExcelIgnore
    private int deviceId;

    @Excel(name = "用户名",width = 15)
    private String deviceName;

    @Excel(name = "密码",width = 15)
    private String password;

    @Excel(name = "用户类型",replace = {"软件用户_0","硬件用户_1"})
    private int userType;

    @ExcelIgnore
    private byte[] encKey;

    @Excel(name = "备注",width = 30)
    private String comments;

    @ExcelIgnore
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelIgnore
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public DeviceUser() {
    }

    public DeviceUser(String deviceName, String password, int userType, String comments) {
        this.deviceName = deviceName;
        this.password = password;
        this.userType = userType;
        this.comments = comments;
    }
}
