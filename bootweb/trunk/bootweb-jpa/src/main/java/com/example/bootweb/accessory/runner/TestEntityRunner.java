package com.example.bootweb.accessory.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestEntityRunner implements CommandLineRunner {

  @Autowired
  private TestEntityRepository testEntityRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    testEntityRepository.save(new TestEntity("test_one"));
    testEntityRepository.save(new TestEntity("test_two"));
    testEntityRepository.save(new TestEntity("test_three"));
    testEntityRepository.save(new TestEntity("test_four"));
    testEntityRepository.save(new TestEntity("test_five"));
    testEntityRepository.save(new TestEntity("test_six"));
    testEntityRepository.save(new TestEntity("test_seven"));
  }

}
