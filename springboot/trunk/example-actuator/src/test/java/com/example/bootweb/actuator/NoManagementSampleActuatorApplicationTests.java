package com.example.bootweb.actuator;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springboot.actuator.Application;

/**
 * Integration tests for switching off management endpoints.
 * {@link https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT, //
    properties = {
        "management.port=-1" })
@DirtiesContext
public class NoManagementSampleActuatorApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testHome() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("user", getPassword())
        .getForEntity("/", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("message")).isEqualTo("Hello Hello");
  }

  @Test
  public void testMetricsNotAvailable() throws Exception {
    testHome(); // makes sure some requests have been made
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("actuator", getPassword())
        .getForEntity("/metrics", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
