package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class BlogZuul {
    public static void main(String[] args){
        SpringApplication.run(BlogZuul.class,args);
    }
}
