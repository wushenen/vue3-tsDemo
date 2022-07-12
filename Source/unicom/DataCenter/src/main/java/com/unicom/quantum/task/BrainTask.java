package com.unicom.quantum.task;

import com.unicom.quantum.service.BrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author jerry
 * @version 1.0
 * @note
 * @date 2022/5/12
 */
@Configuration
@EnableScheduling
public class BrainTask {

    @Autowired
    private BrainService brainService;

    @Scheduled(cron = "30 * * * * ? ")
    private void generateMachineData() throws InterruptedException {
        brainService.getPhysicalMachineInfo();
    }


    @Scheduled(cron = "0 0 0,2,4,6,8,10,12,14,16,18,20,22 * * ? ")
    private void generateCockpitRealTimeData() {
        brainService.geCockpitRealTimeInfo();
    }
}
