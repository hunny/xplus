package com.example.springboot.actuator.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

/**
 * Shows application health information (when the application is secure, a
 * simple ‘status’ when accessed over an unauthenticated connection or full
 * message details when authenticated).
 */
@Component
public class MyHealthIndicator extends AbstractHealthIndicator {

  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    builder.up();
    builder.withDetail("hello", "world");
  }

}
