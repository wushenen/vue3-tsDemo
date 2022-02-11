package com.unicom.quantum;

import com.unicom.quantum.pojo.IpInfo;
import com.unicom.quantum.service.IpService;
import com.unicom.quantum.component.init.Init;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class UnicomManagerApplication implements ApplicationRunner {

    @Autowired
    private IpService ipService;

    public static void main(String[] args) {
        SpringApplication.run(UnicomManagerApplication.class,args);
    }

    //启动时加载配置信息至内存
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<IpInfo> allIps = ipService.getAllIps();
        if (allIps.size() != 0) {
            for (IpInfo ipInfo : allIps) {
                Init.IP_WHITE_SET.add(ipInfo.getIpInfo());
            }
        }
    }
}
