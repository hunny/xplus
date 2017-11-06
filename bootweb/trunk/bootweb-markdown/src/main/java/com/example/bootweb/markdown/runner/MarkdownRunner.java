package com.example.bootweb.markdown.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 3)
public class MarkdownRunner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(MarkdownRunner.class);
  
  public void run(String... args) throws Exception {
    logger.info("第三个顺序执行。");
  }

}
