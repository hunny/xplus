package com.springboot.spark.starter.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * Spark Pi Estimation demo.
 * 
 * Spark can also be used for compute-intensive tasks. This code estimates π by
 * "throwing darts" at a circle. We pick random points in the unit square ((0,
 * 0) to (1,1)) and see how many fall in the unit circle. The fraction should be
 * π / 4, so we use this to get our estimate.
 * 
 * Run with <code>spring.profiles.active=SparkPiEstimation</code>
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("SparkPiEstimation")
public @interface SparkPiEstimation {

  // Do Nothing.

}
