package com.sign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients("com")
@EnableCircuitBreaker
@EnableDiscoveryClient
public class BlogUserSign {
  public static void main(String[] args) {
    SpringApplication.run(BlogUserSign.class, args);
  }
}
