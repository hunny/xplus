package com.example.springboot.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Shows application health information (when the application is secure, a
 * simple ‘status’ when accessed over an unauthenticated connection or full
 * message details when authenticated).
 * 
 */
@Component
public class CustomHealthIndicator implements HealthIndicator {

  @Override
  public Health health() {
    return Health.up().withDetail("hello", "world").build();
  }

}
