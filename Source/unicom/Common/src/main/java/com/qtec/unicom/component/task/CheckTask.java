package com.qtec.unicom.component.task;//package com.qtec.pwsp.component.task;

/*import com.qtec.pwsp.component.init.Init;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;*/

/*@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class CheckTask {
    @Autowired*/
//    private Init Init;

    /**
     * 周期自检
     */
    //0 0 0/1 * * ? 整点   0 0 12 * * ?每天12点
    //或直接指定时间间隔，例如：5秒 */5 * * * * ?
    //0/1 0 0/12 * * ?      从0开始每12小时一次
    //@Scheduled(fixedRate=5000)
/*    @Scheduled(cron = "0/1 0 0/12 * * ?")
    private void deleteKeyTask() {
        System.out.println("周期自检: " + LocalDateTime.now());
        try{
            Init.cycleCheck();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
//}
