package com.example.bootweb.aop;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.springboot.greeter.GreeterService;
import com.example.springboot.greeter.GreeterConfigParams;
import com.example.springboot.greeter.GreetingConfig;

public class ApplicationTest {

  private static GreetingConfig greetingConfig;

  @BeforeClass
  public static void initalizeGreetingConfig() {
    greetingConfig = new GreetingConfig();
    greetingConfig.put(GreeterConfigParams.USER_NAME, "World");
    greetingConfig.put(GreeterConfigParams.MORNING_MESSAGE, "Good Morning");
    greetingConfig.put(GreeterConfigParams.AFTERNOON_MESSAGE, "Good Afternoon");
    greetingConfig.put(GreeterConfigParams.EVENING_MESSAGE, "Good Evening");
    greetingConfig.put(GreeterConfigParams.NIGHT_MESSAGE, "Good Night");
  }

  @Test
  public void givenMorningTime_ifMorningMessage_thenSuccess() {
    String expected = "Hello World, Good Morning";
    GreeterService greeterService = new GreeterService(greetingConfig);
    String actual = greeterService.greet(LocalDateTime.of(2017, 3, 1, 6, 0));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void givenAfternoonTime_ifAfternoonMessage_thenSuccess() {
    String expected = "Hello World, Good Afternoon";
    GreeterService greeterService = new GreeterService(greetingConfig);
    String actual = greeterService.greet(LocalDateTime.of(2017, 3, 1, 13, 0));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void givenEveningTime_ifEveningMessage_thenSuccess() {
    String expected = "Hello World, Good Evening";
    GreeterService greeterService = new GreeterService(greetingConfig);
    String actual = greeterService.greet(LocalDateTime.of(2017, 3, 1, 19, 0));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void givenNightTime_ifNightMessage_thenSuccess() {
    String expected = "Hello World, Good Night";
    GreeterService greeterService = new GreeterService(greetingConfig);
    String actual = greeterService.greet(LocalDateTime.of(2017, 3, 1, 21, 0));
    Assert.assertEquals(expected, actual);
  }
}
