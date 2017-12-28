package com.example.bootweb.actuator;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.actuate.autoconfigure.LocalManagementPort;
import org.springframework.boot.context.embedded.LocalServerPort;
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
 * {@link https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT, //
    properties = {
        "management.port=0" })
@DirtiesContext
public class ManagementPortSampleActuatorApplicationTests {

  @LocalServerPort
  private int port = 9010;

  @LocalManagementPort
  private int managementPort = 9011;

  @Test
  public void testHome() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = new TestRestTemplate("user", getPassword())
        .getForEntity("http://localhost:" + this.port, Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("message")).isEqualTo("Hello Hello");
  }

  @Test
  public void testMetrics() throws Exception {
    testHome(); // makes sure some requests have been made
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = new TestRestTemplate().getForEntity(
        "http://localhost:" + this.managementPort + "/application/metrics", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void testHealth() throws Exception {
    ResponseEntity<String> entity = new TestRestTemplate().withBasicAuth("actuator", getPassword())
        .getForEntity("http://localhost:" + this.managementPort + "/health",
            String.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()).contains("\"status\":\"UP\"");
  }

  @Test
  public void testErrorPage() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = new TestRestTemplate("actuator", getPassword())
        .getForEntity("http://localhost:" + this.managementPort + "/error", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("status")).isEqualTo(999);
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
