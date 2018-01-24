package com.example.bootweb.accessory.druid;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@ConditionalOnBean(DruidConfigValue.class)
public class DruidConfig {

  @Bean // 声明其为Bean实例
  public DataSource dataSource(DruidConfigValue druidConfigValue) throws SQLException {
    DruidDataSource datasource = new DruidDataSource();

    datasource.setUrl(druidConfigValue.getUrl());
    datasource.setUsername(druidConfigValue.getUsername());
    datasource.setPassword(druidConfigValue.getPassword());
    datasource.setDriverClassName(druidConfigValue.getDriverClassName());

    // configuration
    datasource.setInitialSize(druidConfigValue.getInitialSize());
    datasource.setMinIdle(druidConfigValue.getMinIdle());
    datasource.setMaxActive(druidConfigValue.getMaxActive());
    datasource.setMaxWait(druidConfigValue.getMaxWait());
    datasource.setTimeBetweenEvictionRunsMillis(druidConfigValue.getTimeBetweenEvictionRunsMillis());
    datasource.setMinEvictableIdleTimeMillis(druidConfigValue.getMinEvictableIdleTimeMillis());
    datasource.setValidationQuery(druidConfigValue.getValidationQuery());
    datasource.setTestWhileIdle(druidConfigValue.isTestWhileIdle());
    datasource.setTestOnBorrow(druidConfigValue.isTestOnBorrow());
    datasource.setTestOnReturn(druidConfigValue.isTestOnReturn());
    datasource.setPoolPreparedStatements(druidConfigValue.isPoolPreparedStatements());
    datasource.setMaxPoolPreparedStatementPerConnectionSize(//
        druidConfigValue.getMaxPoolPreparedStatementPerConnectionSize());
    datasource.setFilters(druidConfigValue.getFilters());

    return datasource;
  }

}
