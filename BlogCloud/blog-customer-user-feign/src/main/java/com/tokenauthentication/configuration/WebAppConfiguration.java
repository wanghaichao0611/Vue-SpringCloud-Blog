package com.tokenauthentication.configuration;

import com.tokenauthentication.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//拦截所有从前端传过来的请求，必须被@AuthToken注解修饰
//Springboot2.X之后是实现WebMvcConfigurer接口
//Springboot1.X-2.X之间是继承WebMvcConfigurer缺省适配器类
@Configuration
public  class WebAppConfiguration extends WebMvcConfigurerAdapter {

    //拦截所有的URL请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**");
    }
}
