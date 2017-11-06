package com.example.springboot.runner;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.repository.CrudRepository;

public class RepositoryCountRunner implements CommandLineRunner {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private Collection<CrudRepository> repositories;

  public RepositoryCountRunner(Collection<CrudRepository> repositories) {
    this.repositories = repositories;
  }

  public void run(String... args) throws Exception {
    repositories.forEach(crudRepository -> {
      logger.info(String.format("%s has %s entries", getRepositoryName(crudRepository.getClass()),
          crudRepository.count()));
    });

  }

  private static String getRepositoryName(Class crudRepositoryClass) {
    for (Class repositoryInterface : crudRepositoryClass.getInterfaces()) {
      return repositoryInterface.getSimpleName();
    }
    return "UnknownRepository";
  }

}
