package com.springboot.spark.starter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    System.out.println("Run starter inside.");
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
