package com.unicom.quantum.service;

import com.unicom.quantum.pojo.CardData;
import com.unicom.quantum.controller.vo.LinuxServerRequest;
import com.unicom.quantum.pojo.QkmVersion;

import java.util.List;

public interface SystemManageService {

    String updateIpNetmaskAndGateway(LinuxServerRequest linuxServerRequest) throws Exception;

    QkmVersion getQkmVersion();

    String init()throws Exception;

    void backUp(String backPass)throws Exception;

    String restore(CardData cardData)throws Exception;

    List<CardData> listCardData(CardData cardData)throws Exception;
}
