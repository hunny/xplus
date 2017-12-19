package com.example.bootweb.accessory.runner;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
//@RepositoryRestResource
public interface TestEntityRepository extends PagingAndSortingRepository<TestEntity, Long> {

  @Query("select t from TestEntity t")
  Stream<TestEntity> findEntities();
  
}
