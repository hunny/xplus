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
 * Integration tests for endpoints configuration.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT, //
    properties = {
        "management.context-path=/admin" //
    })
@DirtiesContext
public class ManagementPathSampleActuatorApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testHealth() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.withBasicAuth("user", getPassword())
        .getForEntity("/admin/health", String.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()).contains("\"status\":\"UP\"");
  }

  @Test
  @SuppressWarnings(value = {
      "rawtypes", "unchecked" })
  public void testHomeIsSecure() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("/", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("error")).isEqualTo("Unauthorized");
    Assertions.assertThat(entity.getHeaders()).doesNotContainKey("Set-Cookie");
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
