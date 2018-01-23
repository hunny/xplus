package com.example.bootweb.accessory.dao.fiveone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FiveOneDao {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  public Long insert() {
    StringBuffer buffer = new StringBuffer();
    return 1L;
  }

}
