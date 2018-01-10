package com.example.bootweb.actuator.noweb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springboot.actuator.noweb.Application;
import com.example.springboot.actuator.noweb.service.HelloWorldService;

/**
 * Basic integration tests for service demo application.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext
public class SampleActuatorNoWebApplicationTests {

  @Autowired
  private HelloWorldService helloWorldService;

  @Value("${service.name}")
  private String message;

  @Test
  public void contextLoads() {
    Assert.assertEquals("Message ", helloWorldService.getHelloMessage().startsWith(message), true);
  }

}
