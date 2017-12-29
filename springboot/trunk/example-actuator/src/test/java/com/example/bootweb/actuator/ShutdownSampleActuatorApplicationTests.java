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
 * Integration tests for separate management and main service ports.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ShutdownSampleActuatorApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @SuppressWarnings(value = {
      "rawtypes", "unchecked" })
  public void testHome() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("user", getPassword())
        .getForEntity("/", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("message")).isEqualTo("Hello Hello");
  }

  @Test
  @SuppressWarnings(value = {
      "rawtypes", "unchecked" })
  public void testShutdown() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("actuator", getPassword())
        .postForEntity("/shutdown", null, Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(((String) body.get("message"))).contains("Shutting down");
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
