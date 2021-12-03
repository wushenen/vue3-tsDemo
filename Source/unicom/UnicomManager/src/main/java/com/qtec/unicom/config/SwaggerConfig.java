package com.qtec.unicom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("qtec")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qtec.unicom"))
                .paths(PathSelectors.any())
                .build();
    }

    //配置接口信息
    private ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("jerry", "", "xulx@qtec.cn");

        return new ApiInfo("混合量子加密平台API管理平台"
                , "混合量子加密平台接口"
                , "1.0"
                , "http://192.168.90.96/213"
                ,  contact
                , "Apache 2.0"
                , "http://www.apache.org/licenses/LICENSE-2.0"
                , new ArrayList<VendorExtension>());
    }

}
