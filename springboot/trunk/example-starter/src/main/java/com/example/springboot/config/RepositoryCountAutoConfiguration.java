package com.example.springboot.config;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

import com.example.springboot.runner.RepositoryCountRunner;

@Configuration
public class RepositoryCountAutoConfiguration {

  @Bean
  public RepositoryCountRunner dbCountRunner(Collection<CrudRepository> repositories) {
    return new RepositoryCountRunner(repositories);
  }

}
