/*
package com.qtec.mixedquantum.component.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

*/
/**
 * Created by wuzh on 2020/1/15.
 * Describe: 注册自定义拦截器TokenInterceptor，使自定义拦截器生效，并配置不需要拦截的uri
 *//*


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public IpAccessInterceptor ipAccessInterceptor() {
        return new IpAccessInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAccessInterceptor()).excludePathPatterns("**");
    }
}
*/
