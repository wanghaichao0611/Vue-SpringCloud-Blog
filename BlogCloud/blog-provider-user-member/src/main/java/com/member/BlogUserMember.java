package com.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients("com")
@EnableCircuitBreaker
@EnableScheduling
@EnableDiscoveryClient
public class BlogUserMember {
  public static void main(String[] args) {
    SpringApplication.run(BlogUserMember.class, args);
  }
}
