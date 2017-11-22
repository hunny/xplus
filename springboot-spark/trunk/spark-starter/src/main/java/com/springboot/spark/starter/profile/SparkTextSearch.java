package com.springboot.spark.starter.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * Spark Text Search demo.
 * 
 * Run with <code>spring.profiles.active=SparkTextSearch</code>
 * 
 * and use the file <code>spark.text.search.file=your file path</code> to search.
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("SparkTextSearch")
public @interface SparkTextSearch {

  //Do Nothing.
  
}
