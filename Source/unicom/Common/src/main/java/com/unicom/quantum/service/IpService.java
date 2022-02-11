package com.unicom.quantum.service;

import com.unicom.quantum.component.Exception.QuantumException;
import com.unicom.quantum.pojo.IpInfo;

import java.util.List;

public interface IpService {

    int addIp(String ipInfo) throws QuantumException;

    List<IpInfo> getAllIps();

    int deleteIpById(String ipInfo);

}
