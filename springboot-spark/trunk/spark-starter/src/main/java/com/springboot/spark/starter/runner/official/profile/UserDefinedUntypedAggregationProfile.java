package com.springboot.spark.starter.runner.official.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * Run with <code>spring.profiles.active=UserDefinedUntypedAggregationProfile</code>
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("UserDefinedUntypedAggregationProfile")
public @interface UserDefinedUntypedAggregationProfile {

}
