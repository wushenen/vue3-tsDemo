package com.cucc.unicom.service.impl;

import com.cucc.unicom.mapper.IntercomStatusMapper;
import com.cucc.unicom.pojo.IntercomStatus;
import com.cucc.unicom.service.IntercomStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntercomStatusServiceImpl implements IntercomStatusService {
    
    @Autowired
    private IntercomStatusMapper intercomStatusMapper;
    
    @Override
    public void updateIntercom(IntercomStatus intercomStatus) {
        if (intercomStatusMapper.intercomExist(intercomStatus.getDeviceName())) {
            intercomStatusMapper.updateIntercom(intercomStatus);
        }else {
            intercomStatusMapper.addIntercom(intercomStatus);
        }
    }

    @Override
    public List<IntercomStatus> getIntercomStatus() {
        return intercomStatusMapper.getIntercomStatus();
    }
}
