package com.example.bootweb.security.conditional;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@Import(ConditionUsingClass.class)
public class ConditionalTestApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(ConditionalTestApplication.class);
    springApplication.setWebEnvironment(false);
    ConfigurableApplicationContext noneMessageConfigurableApplicationContext = springApplication
        .run("--logging.level.root=ERROR");
    try {
      noneMessageConfigurableApplicationContext.getBean(HelloWorldService.class).announce();
    } catch (Exception e) {
      e.printStackTrace();//此处报错，是预想中的
    }
    ConfigurableApplicationContext configurableApplicationContext = springApplication
        .run("--my.name=Normal", "--logging.level.root=ERROR");
    configurableApplicationContext.getBean(HelloWorldService.class).announce();
  }

}