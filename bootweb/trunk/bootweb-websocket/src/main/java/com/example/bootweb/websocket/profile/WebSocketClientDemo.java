package com.example.bootweb.websocket.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * Demo using Java Client to Handle websocket message.
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("WebSocketClientDemo")
public @interface WebSocketClientDemo {
  // Do Nothing. Run as -Dspring.profiles.active=WebSocketDemo
}
