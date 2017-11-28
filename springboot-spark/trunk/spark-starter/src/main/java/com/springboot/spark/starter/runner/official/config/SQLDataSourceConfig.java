package com.springboot.spark.starter.runner.official.config;

import org.springframework.context.annotation.Configuration;

import com.springboot.spark.starter.config.SparkConfig;
import com.springboot.spark.starter.runner.official.profile.SQLDataSourceProfile;

@Configuration
@SQLDataSourceProfile
public class SQLDataSourceConfig extends SparkConfig {

  //Do Nothing.
  
}
