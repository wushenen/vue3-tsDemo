package com.qtec.unicom.service;

import com.qtec.unicom.pojo.CardData;
import com.qtec.unicom.pojo.LinuxServer;
import com.qtec.unicom.pojo.QkmVersion;

import java.util.List;

public interface SystemMangeService {
//    QkmVersion getQkmVersion();

    String updateIpNetmaskAndGateway(LinuxServer linuxServer) throws Exception;

    QkmVersion getQkmVersion() throws Exception;

    String init()throws Exception;

    void backUp(String backPass)throws Exception;

    String restore(CardData cardData)throws Exception;

    List<CardData> listCardData(CardData cardData)throws Exception;
}
