package com.example.bootweb.accessory.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.bootweb.accessory.profile.TopeaseProfile;

@TopeaseProfile
@Configuration
public class MysqlConfig {

  @Bean
  public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public DataSource dataSource() {
    // return DataSourceBuilder.create() //
    // .driverClassName(env.getProperty("mysql.jdbc.driverClassName")) //
    // .username(env.getProperty("mysql.jdbc.username")) //
    // .password(env.getProperty("mysql.jdbc.password")) //
    // .url(env.getProperty("mysql.jdbc.url")) //
    // .build();
    return DataSourceBuilder.create() //
        .driverClassName("com.mysql.jdbc.Driver") //
        .username("root") //
        .password("mysqlpasswd") //
        .url("jdbc:mysql://localhost:4306/topease") //
        .build();
  }

}
