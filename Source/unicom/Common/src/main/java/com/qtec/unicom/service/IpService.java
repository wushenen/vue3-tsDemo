package com.qtec.unicom.service;

import com.qtec.unicom.pojo.IpInfo;

import java.util.List;

public interface IpService {

    int addIp(String ipInfo);

    List<IpInfo> getAllIps();

    int deleteIpById(String ipInfo);

}
