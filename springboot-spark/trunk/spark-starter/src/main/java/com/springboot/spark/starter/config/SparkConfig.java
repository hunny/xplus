package com.springboot.spark.starter.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.springboot.spark.starter.profile.SparkWordCount;

@Configuration
@PropertySource("classpath:META-INF/spark/spark-config.properties")
@SparkWordCount
public class SparkConfig {

  @Value("${app.name:jigsaw}")
  private String appName;

  @Value("${spark.home}")
  private String sparkHome;

  @Value("${master.uri:local}")
  private String masterUri;

  @Bean
  public SparkConf sparkConf() {
    SparkConf sparkConf = new SparkConf().setAppName(appName).setSparkHome(sparkHome)
        .setMaster(masterUri);

    return sparkConf;
  }

  @Bean
  public JavaSparkContext javaSparkContext() {
    return new JavaSparkContext(sparkConf());
  }

  @Bean
  public SparkSession sparkSession() {
    return SparkSession.builder().sparkContext(javaSparkContext().sc())
        .appName("Java Spark SQL basic example").getOrCreate();
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
