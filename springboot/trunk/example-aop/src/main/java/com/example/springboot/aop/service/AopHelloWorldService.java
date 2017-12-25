package com.example.springboot.aop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AopHelloWorldService {

  @Value("${name:World}")
  private String name;

  public String getHelloMessage() {
    return "Hello " + this.name;
  }

}
