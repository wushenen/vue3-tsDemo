package com.cucc.unicom.component.fastJsonConfig;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cucc.unicom.component.interceptor.IpAccessInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyConfigs extends WebMvcConfigurationSupport {


     @Override
     public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                 FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
                 FastJsonConfig config = new FastJsonConfig();
                 config.setSerializerFeatures(
                                // 保留 Map 空的字段
                                 SerializerFeature.WriteMapNullValue,
                                 // 将 String 类型的 null 转成""
                                 SerializerFeature.WriteNullStringAsEmpty,
                                // 将 Number 类型的 null 转成 0
//                                 SerializerFeature.WriteNullNumberAsZero,
                                 // 将 List 类型的 null 转成 []
                                SerializerFeature.WriteNullListAsEmpty,
                                // 将 Boolean 类型的 null 转成 false
//                                SerializerFeature.WriteNullBooleanAsFalse,
                                SerializerFeature.WriteDateUseDateFormat,
                                // 避免循环引用
                               SerializerFeature.DisableCircularReferenceDetect);
//                 config.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
         config.setDateFormat("yyyy-MM-dd HH:mm:ss");
         converter.setFastJsonConfig(config);

                 converter.setDefaultCharset(Charset.forName("UTF-8"));
                List<MediaType> mediaTypeList = new ArrayList<>();
                 // 解决中文乱码问题，相当于在 Controller 上的 @RequestMapping 中加了个属性 produces = "application/json"
                 mediaTypeList.add(MediaType.APPLICATION_JSON);
                 converter.setSupportedMediaTypes(mediaTypeList);
                converters.add(converter);
     }

    /**
     * 资源处理
      * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("index.html","/","/**")
                .addResourceLocations("classpath:/");
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui/index.html");
        super.addViewControllers(registry);
    }

    @Bean
    public IpAccessInterceptor ipAccessInterceptor(){
        return new IpAccessInterceptor();
    }

    /**
     * 自定义ip拦截器注册
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAccessInterceptor()).excludePathPatterns("**");
        super.addInterceptors(registry);
    }
}
