package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;


@SpringBootApplication
@EnableEurekaClient
@EnableTurbineStream
public class BlogTurbine {
  public static void main(String[] args) {
    SpringApplication.run(BlogTurbine.class, args);
  }

}
