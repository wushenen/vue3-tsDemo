package com.cucc.unicom.service;

import com.cucc.unicom.pojo.CardData;
import com.cucc.unicom.pojo.LinuxServer;
import com.cucc.unicom.pojo.QkmVersion;

import java.util.List;

public interface SystemManageService {

    String updateIpNetmaskAndGateway(LinuxServer linuxServer) throws Exception;

    QkmVersion getQkmVersion();

    String init()throws Exception;

    void backUp(String backPass)throws Exception;

    String restore(CardData cardData)throws Exception;

    List<CardData> listCardData(CardData cardData)throws Exception;
}
