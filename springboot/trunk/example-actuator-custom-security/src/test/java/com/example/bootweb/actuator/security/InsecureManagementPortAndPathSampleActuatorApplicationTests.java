package com.example.bootweb.actuator.security;

import static org.assertj.core.api.Assertions.assertThat;

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

import com.example.springboot.actuator.security.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT, //
    properties = {
        "management.port=0", //
        "management.context-path=/management" //
    })
@DirtiesContext
public class InsecureManagementPortAndPathSampleActuatorApplicationTests {

  @LocalServerPort
  private int port = 9010;

  @LocalManagementPort
  private int managementPort = 9011;

  @Test
  public void testHome() throws Exception {
    ResponseEntity<String> entity = new TestRestTemplate("user", "password")
        .getForEntity("http://localhost:" + this.port, String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody()).contains("Hello World");
  }

  @Test
  public void testSecureActuator() throws Exception {
    ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
        "http://localhost:" + this.managementPort + "/management/health", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    // SpringBoot 2.+
//    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void testInsecureActuator() throws Exception {
//    ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
//        "http://localhost:" + this.managementPort + "/management/status", String.class);
//    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertThat(entity.getBody()).contains("\"status\":\"UP\"");
  }

  @Test
  public void testMissing() throws Exception {
    ResponseEntity<String> entity = new TestRestTemplate("admin", "admin").getForEntity(
        "http://localhost:" + this.managementPort + "/management/missing",
        String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(entity.getBody()).contains("\"status\":404");
  }

}
