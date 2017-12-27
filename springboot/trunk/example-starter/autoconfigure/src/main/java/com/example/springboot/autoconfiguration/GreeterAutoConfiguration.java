package com.example.springboot.autoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springboot.greeter.GreeterService;
import com.example.springboot.greeter.GreeterConfigParams;
import com.example.springboot.greeter.GreetingConfig;

@Configuration
@ConditionalOnClass(GreeterService.class)
@EnableConfigurationProperties(GreeterProperties.class)
public class GreeterAutoConfiguration {

  @Autowired
  private GreeterProperties greeterProperties;

  @Bean
  @ConditionalOnMissingBean
  public GreetingConfig greeterConfig() {

    String userName = greeterProperties.getUserName() == null ? System.getProperty("user.name")
        : greeterProperties.getUserName();
    String morningMessage = greeterProperties.getMorningMessage() == null ? "Good Morning"
        : greeterProperties.getMorningMessage();
    String afternoonMessage = greeterProperties.getAfternoonMessage() == null ? "Good Afternoon"
        : greeterProperties.getAfternoonMessage();
    String eveningMessage = greeterProperties.getEveningMessage() == null ? "Good Evening"
        : greeterProperties.getEveningMessage();
    String nightMessage = greeterProperties.getNightMessage() == null ? "Good Night"
        : greeterProperties.getNightMessage();

    GreetingConfig greetingConfig = new GreetingConfig();
    greetingConfig.put(GreeterConfigParams.USER_NAME, userName);
    greetingConfig.put(GreeterConfigParams.MORNING_MESSAGE, morningMessage);
    greetingConfig.put(GreeterConfigParams.AFTERNOON_MESSAGE, afternoonMessage);
    greetingConfig.put(GreeterConfigParams.EVENING_MESSAGE, eveningMessage);
    greetingConfig.put(GreeterConfigParams.NIGHT_MESSAGE, nightMessage);
    return greetingConfig;
  }

  @Bean
  @ConditionalOnMissingBean
  public GreeterService greeterService(GreetingConfig greetingConfig) {
    return new GreeterService(greetingConfig);
  }

}
