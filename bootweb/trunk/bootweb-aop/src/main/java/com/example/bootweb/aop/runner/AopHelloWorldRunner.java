package com.example.bootweb.aop.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bootweb.aop.service.AopHelloWorldService;

@Component
public class AopHelloWorldRunner implements CommandLineRunner {

  // Simple example shows how an application can spy on itself with AOP

  @Autowired
  private AopHelloWorldService helloWorldService;
  
  @Override
  public void run(String... arg0) throws Exception {
    System.out.println(this.helloWorldService.getHelloMessage());
  }

}
