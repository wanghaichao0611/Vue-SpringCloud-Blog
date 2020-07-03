package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//高可用EurekaServer
@SpringBootApplication
@EnableEurekaServer
public class HighEurekaServer {
  public static void main(String[] args) {
    SpringApplication.run(HighEurekaServer.class, args);
  }
}
