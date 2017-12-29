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
 * 
 * {@link https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT, //
    properties = {
        "server.servlet-path=/spring" //# Path of the main dispatcher servlet.
        //not `"server.context-path=/spring"` # Context path of the application.
    })
@DirtiesContext
public class ServletPathSampleActuatorApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testErrorPath() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("actuator", getPassword())
        .getForEntity("/spring/error", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("error")).isEqualTo("None");
    Assertions.assertThat(body.get("status")).isEqualTo(999);
  }

  @Test
  public void testHealth() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.withBasicAuth("actuator", getPassword())
        .getForEntity("/spring/health", String.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()).contains("\"status\":\"UP\"");
  }

  @Test
  public void testHomeIsSecure() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("/spring/", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("error")).isEqualTo("Unauthorized");
    Assertions.assertThat(entity.getHeaders()).doesNotContainKey("Set-Cookie");
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
