package com.example.bootweb.tpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableTask
public class Application {

  @Bean
  public CommandLineRunner commandLineRunner() {
    return strings -> System.out
        .println("Executed at :" + new SimpleDateFormat().format(new Date()));
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
