package com.springboot.spark.starter.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * Spark Word Count demo.
 * 
 * Run with <code>spring.profiles.active=SparkWordCount</code>
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("SparkWordCount")
public @interface SparkWordCount {

  //Do Nothing.
  
}
