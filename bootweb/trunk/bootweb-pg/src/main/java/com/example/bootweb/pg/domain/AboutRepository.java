package com.example.bootweb.pg.domain;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AboutRepository extends JpaRepository<About, Long> {

  List<About> findByName(String name);

  @Query(value = "SELECT * FROM about " //
      + " WHERE 1 = 1 " //
      + " AND name = :name ", nativeQuery = true)
  List<About> listByName(@Param("name") String name);

  @Query(value = "SELECT * FROM about " //
      + " WHERE 1 = 1 " //
      + " AND name like %:name% ", nativeQuery = true)
  List<About> listLikeName(@Param("name") String name);

  @Query(value = "SELECT id FROM about " //
      + " WHERE 1 = 1 " //
      + " AND name like %:name% ", nativeQuery = true)
  List<BigInteger> listIdLikeName(@Param("name") String name);
  
}
