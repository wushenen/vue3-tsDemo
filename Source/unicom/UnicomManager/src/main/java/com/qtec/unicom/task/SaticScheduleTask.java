package com.qtec.unicom.task;

import com.qtec.unicom.service.PrimaryKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Autowired
    private PrimaryKeyService primaryKeyService;
    /**
     * 删除主密钥
     */
    @Scheduled(cron = "0 0 2 * * ?")
    private void deleteKeyTask() {
        System.out.println("删除主密钥定时任务时间: " + LocalDateTime.now());
        try{
            primaryKeyService.autoDeleteKey();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 自动轮转
     */
    @Scheduled(cron = "0 0 23 * * ?")
    private void rotationTask() {
        System.out.println("自动轮转定时任务时间: " + LocalDateTime.now());
        try{
            primaryKeyService.automatic();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除凭据对象
     */
    /*@Scheduled(cron = "0 0 1 * * ?")
    private void deleteSecretTask(){
        System.out.println("删除凭据对象定时任务时间: " + LocalDateTime.now());
        try{
            secretService.autoDeleteSecret();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}




