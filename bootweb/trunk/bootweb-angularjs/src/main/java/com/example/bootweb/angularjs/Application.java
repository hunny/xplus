package com.example.bootweb.angularjs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.bootweb.swagger.EnableSwagger2Api;

@SpringBootApplication
@EnableSwagger2Api
public class Application {
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
}
