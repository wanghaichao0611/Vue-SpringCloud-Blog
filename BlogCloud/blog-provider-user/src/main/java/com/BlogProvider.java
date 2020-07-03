package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@EnableFeignClients("com")
@EnableCircuitBreaker
@EnableDiscoveryClient
public class BlogProvider
{
    public static void main(String[] args)
    {
        SpringApplication.run(BlogProvider.class,args);
    }
    
    //初始化并且加载JSON转换器
    @Bean(name = "requestMappingHandlerAdapter")
    public HandlerAdapter initRequestMappingHandlerAdapter()
    {
        //创建RequsetMappingHandlerAdapter适配器
        RequestMappingHandlerAdapter rmhd=new RequestMappingHandlerAdapter();
        //HTTP JSON转换器
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter=new MappingJackson2HttpMessageConverter();
        //MappingJackson2HttpMessageConverter接受JSON类型消息的转换
        MediaType mediaType=MediaType.APPLICATION_JSON_UTF8;
        List<MediaType> mediaTypes=new ArrayList<MediaType>();
        mediaTypes.add(mediaType);
        //加入转换器的支持类型
        jackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        rmhd.getMessageConverters().add(jackson2HttpMessageConverter);
        return rmhd;
    }
}
