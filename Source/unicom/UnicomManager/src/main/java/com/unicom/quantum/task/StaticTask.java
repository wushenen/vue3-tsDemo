package com.unicom.quantum.task;

import com.unicom.quantum.service.DeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
public class StaticTask {

    @Autowired
    private DeviceStatusService deviceStatusService;

    @Scheduled(fixedRate=300000)
    private void checkDeviceStatus(){
        deviceStatusService.checkDeviceStatus(null,0);
    }

}
