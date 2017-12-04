package com.springboot.spark.starter.runner.official.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * Transitive closure on a graph.
 * 
 * Run with <code>spring.profiles.active=TransitiveGraphClosure</code>
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("TransitiveGraphClosure")
public @interface TransitiveGraphClosureProfile {

}
