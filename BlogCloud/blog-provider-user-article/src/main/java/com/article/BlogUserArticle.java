package com.article;

import com.article.Config.RedisSonConfig;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com")
@EnableCircuitBreaker
@EnableDiscoveryClient
public class BlogUserArticle {
  public static void main(String[] args) {
    SpringApplication.run(BlogUserArticle.class, args);
  }
}
