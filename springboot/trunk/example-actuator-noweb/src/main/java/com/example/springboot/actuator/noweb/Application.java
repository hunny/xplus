package com.example.springboot.actuator.noweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.springboot.actuator.noweb.config.ServiceProperties;

@SpringBootApplication
@EnableConfigurationProperties(ServiceProperties.class)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
