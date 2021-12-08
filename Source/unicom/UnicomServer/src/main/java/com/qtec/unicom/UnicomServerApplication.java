package com.qtec.unicom;

import com.qtec.unicom.component.init.Init;
import com.qtec.unicom.pojo.IpInfo;
import com.qtec.unicom.service.IpService;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class UnicomServerApplication implements ApplicationRunner {

    @Autowired
    private IpService ipService;

    public static void main(String[] args) {
        SpringApplication.run(UnicomServerApplication.class,args);
    }

    /**
     * 将http请求变成https请求
     * @return
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }
    /**
     * 将http请求变成https请求
     * @return
     */
    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8889);
        return connector;
    }

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
