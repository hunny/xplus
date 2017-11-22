package com.springboot.spark.starter.runner.official.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * Spark Word Count read from file and output to files with runner demo.
 * 
 * Run with <code>spring.profiles.active=WordCount</code>
 * 
 * and args with `c:/demo.txt`
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("WordCount")
public @interface WordCountProfile {

}
