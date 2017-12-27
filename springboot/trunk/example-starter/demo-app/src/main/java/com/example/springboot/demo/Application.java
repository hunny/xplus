package com.example.springboot.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.springboot.greeter.GreeterService;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  private GreeterService greeter;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    String message = greeter.greet();
    System.out.println(message);
  }
}
