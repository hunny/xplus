package com.example.bootweb.actuator.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springboot.actuator.security.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class SampleActuatorCustomSecurityApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @SuppressWarnings(value = {
      "rawtypes", "unchecked" })
  public void homeIsSecure() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("/", Map.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    Map<String, Object> body = entity.getBody();
    assertThat(body.get("error")).isEqualTo("Unauthorized");
    assertThat(entity.getHeaders()).doesNotContainKey("Set-Cookie");
  }

  @Test
  @SuppressWarnings(value = {
      "rawtypes", "unchecked" })
  public void testInsecureApplicationPath() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("/foo", Map.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    Map<String, Object> body = entity.getBody();
    assertThat((String) body.get("message")).contains("Expected exception in controller");
  }

  @Test
  public void testInsecureStaticResources() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/css/bootstrap.min.css",
        String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody()).contains("body");
  }

  @Test
  public void insecureActuator() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/status", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody()).contains("\"status\":\"UP\"");
  }

  @Test
  @SuppressWarnings("rawtypes")
  public void secureActuator() throws Exception {
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("/env", Map.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

}
