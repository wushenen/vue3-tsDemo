package com.cucc.unicom.service;

import com.cucc.unicom.pojo.IntercomStatus;

import java.util.List;

public interface IntercomStatusService {

    void updateIntercom(IntercomStatus intercomStatus);

    List<IntercomStatus> getIntercomStatus();
}
