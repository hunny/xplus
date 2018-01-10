package com.example.springboot.actuator.noweb.service;

import org.springframework.stereotype.Component;

import com.example.springboot.actuator.noweb.config.ServiceProperties;

@Component
public class HelloWorldService {

  private final ServiceProperties configuration;

  public HelloWorldService(ServiceProperties configuration) {
    this.configuration = configuration;
  }

  public String getHelloMessage() {
    return "Hello " + this.configuration.getName();
  }

}
