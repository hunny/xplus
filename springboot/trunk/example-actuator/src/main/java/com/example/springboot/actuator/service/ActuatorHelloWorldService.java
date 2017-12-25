package com.example.springboot.actuator.service;

import org.springframework.stereotype.Component;

import com.example.springboot.actuator.config.ServiceProperties;

@Component
public class ActuatorHelloWorldService {

  private final ServiceProperties configuration;

  public ActuatorHelloWorldService(ServiceProperties configuration) {
    this.configuration = configuration;
  }

  public String getHelloMessage() {
    return "Hello " + this.configuration.getName();
  }

}
