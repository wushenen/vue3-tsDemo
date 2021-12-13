package com.cucc.unicom.config;

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
        Contact contact = new Contact("qtec", "", "");

        return new ApiInfo("联通密钥云管理平台"
                , "联通密钥云管理平台在线接口"
                , "1.0"
                , ""
                ,  contact
                , ""
                , ""
                , new ArrayList<VendorExtension>());
    }

}
