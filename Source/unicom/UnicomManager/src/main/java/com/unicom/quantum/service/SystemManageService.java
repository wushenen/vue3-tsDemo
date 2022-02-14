package com.unicom.quantum.service;

import com.unicom.quantum.controller.vo.LinuxServerRequest;

import java.util.Map;

public interface SystemManageService {

    String updateIpNetmaskAndGateway(LinuxServerRequest linuxServerRequest) throws Exception;

    Map<String,String> getQkmVersion();

    String init()throws Exception;
}
