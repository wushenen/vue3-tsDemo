package com.cucc.unicom.service;

import com.cucc.unicom.pojo.IpInfo;

import java.util.List;

public interface IpService {

    int addIp(String ipInfo);

    List<IpInfo> getAllIps();

    int deleteIpById(String ipInfo);

}
