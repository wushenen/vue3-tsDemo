package com.unicom.quantum.service;

import com.unicom.quantum.pojo.CardData;
import com.unicom.quantum.pojo.LinuxServer;
import com.unicom.quantum.pojo.QkmVersion;

import java.util.List;

public interface SystemManageService {

    String updateIpNetmaskAndGateway(LinuxServer linuxServer) throws Exception;

    QkmVersion getQkmVersion();

    String init()throws Exception;

    void backUp(String backPass)throws Exception;

    String restore(CardData cardData)throws Exception;

    List<CardData> listCardData(CardData cardData)throws Exception;
}
