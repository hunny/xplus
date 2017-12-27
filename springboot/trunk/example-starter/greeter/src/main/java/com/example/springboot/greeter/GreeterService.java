package com.example.springboot.greeter;

import java.time.LocalDateTime;

public class GreeterService {

  private GreetingConfig greetingConfig;

  public GreeterService(GreetingConfig greetingConfig) {
    this.greetingConfig = greetingConfig;
  }

  public String greet(LocalDateTime localDateTime) {

    String name = greetingConfig.getProperty(GreeterConfigParams.USER_NAME);
    int hourOfDay = localDateTime.getHour();

    if (hourOfDay >= 5 && hourOfDay < 12) {
      return String.format("Hello %s, %s", name,
          greetingConfig.get(GreeterConfigParams.MORNING_MESSAGE));
    } else if (hourOfDay >= 12 && hourOfDay < 17) {
      return String.format("Hello %s, %s", name,
          greetingConfig.get(GreeterConfigParams.AFTERNOON_MESSAGE));
    } else if (hourOfDay >= 17 && hourOfDay < 20) {
      return String.format("Hello %s, %s", name,
          greetingConfig.get(GreeterConfigParams.EVENING_MESSAGE));
    } else {
      return String.format("Hello %s, %s", name,
          greetingConfig.get(GreeterConfigParams.NIGHT_MESSAGE));
    }
  }

  public String greet() {
    return greet(LocalDateTime.now());
  }

}
