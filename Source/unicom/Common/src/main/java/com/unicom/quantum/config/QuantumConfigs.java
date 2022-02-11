package com.unicom.quantum.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.unicom.quantum.component.interceptor.IpAccessInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class QuantumConfigs extends WebMvcConfigurationSupport {


     @Override
     public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
         FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
         FastJsonConfig config = new FastJsonConfig();
         config.setSerializerFeatures(
                 SerializerFeature.WriteMapNullValue,
                 SerializerFeature.WriteNullStringAsEmpty,
                 SerializerFeature.WriteNullNumberAsZero,
                 SerializerFeature.WriteNullListAsEmpty,
                 SerializerFeature.WriteNullBooleanAsFalse,
                 SerializerFeature.WriteDateUseDateFormat,
                 SerializerFeature.DisableCircularReferenceDetect);
         config.setDateFormat("yyyy-MM-dd HH:mm:ss");
         converter.setFastJsonConfig(config);
         converter.setDefaultCharset(Charset.forName("UTF-8"));
         List<MediaType> mediaTypeList = new ArrayList<>();
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
