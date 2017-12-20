package com.example.bootweb.accessory.runner;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
class TestEntityLogger {

  private static final Logger logger = LoggerFactory.getLogger(TestEntityLogger.class);

  private final TestEntityRepository testEntityRepository;

  @Autowired
  TestEntityLogger(TestEntityRepository testEntityRepository) {
    this.testEntityRepository = testEntityRepository;
  }

  @Scheduled(fixedDelay = 60000, initialDelay = 5000)
  @Transactional(readOnly = true)
  public void logAllTestEntities() {
    System.out.println("开始启动。");
    try (Stream<TestEntity> entities = testEntityRepository.findEntities()) {
      entities.forEach(e -> logger.info(e.toString()));
    }
    logger.info("Finished reading the stream. It should be closed by now.");
    System.out.println("执行完毕。");
  }
}
