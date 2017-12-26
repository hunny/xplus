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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springboot.actuator.Application;

/**
 * Integration tests for endpoints configuration.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("endpoints")
public class EndpointsPropertiesSampleActuatorApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testCustomErrorPath() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("user", getPassword())
        .getForEntity("/oops", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("error")).isEqualTo("None");
    Assertions.assertThat(body.get("status")).isEqualTo(999);
  }

  @Test
  public void testCustomContextPath() throws Exception {
    // Shows application health information (when the application is secure, a
    // simple ‘status’ when accessed over an unauthenticated connection or full
    // message details when authenticated).
    ResponseEntity<String> entity = this.restTemplate.withBasicAuth("actuator", getPassword())
        .getForEntity("/admin/health", String.class);
    System.out.println(entity.getBody());
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()).contains("\"status\":\"UP\"");
    Assertions.assertThat(entity.getBody()).contains("\"hello\":\"world\"");
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
