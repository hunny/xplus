package com.springboot.spark.starter.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySource("classpath:META-INF/spark/spark-config.properties")
public class SparkConfig {

  @Value("${app.name:HunnyHu}")
  private String appName;

  @Value("${spark.home}")
  private String sparkHome;

  @Value("${master.uri:local}")
  private String masterUri;

  @Bean
  public SparkConf sparkConf() {
    SparkConf sparkConf = new SparkConf() //
        .setAppName(appName) // The name of application. This will appear in the
                             // UI and in log data.
        .setSparkHome(sparkHome) //
        .setMaster(masterUri) //

        // Port for application's dashboard, which shows memory and workload
        // data.
        .set("spark.ui.port", "7077") //

        // Which scales the number of executors registered with this application
        // up and down based on the workload
        .set("dynamicAllocation.enabled", "false") //

        // For serializing objects that will be sent over the network or need to
        // be cached in serialized form.
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer") //
        
        .set("spark.local.dir", "D:/spark") //work with -Dspark.local.dir=D:/spark

        .set("spark.driver.allowMultipleContexts", "true");

    return sparkConf;
  }

  @Bean
  public JavaSparkContext javaSparkContext(SparkConf sparkConf) {
    return new JavaSparkContext(sparkConf);
  }

  @Bean
  public SparkSession sparkSession(JavaSparkContext javaSparkContext) {
    return SparkSession.builder().sparkContext(javaSparkContext.sc())
        .appName("Java Spark SQL basic example").getOrCreate();
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
