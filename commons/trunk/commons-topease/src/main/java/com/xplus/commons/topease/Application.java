package com.xplus.commons.topease;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 应用程序主入口
 * 
 * @author huzexiong
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
  
  public static void main(String[] args) throws Exception {
    ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
        .headless(false).run(args);
    SwingApp appFrame = context.getBean(SwingApp.class);
    appFrame.run(args);
  }
  
}
