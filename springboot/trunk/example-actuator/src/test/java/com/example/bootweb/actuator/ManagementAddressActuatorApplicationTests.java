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
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT, //
    properties = {
        "management.server.port=0", //
        "management.server.address=127.0.0.1", //
        "management.server.context-path=/admin" //
//        "endpoints.health.path=/admin" //
    })
@DirtiesContext
public class ManagementAddressActuatorApplicationTests {

  @LocalServerPort
  private int port = 9010;

  @LocalManagementPort
  private int managementPort = 9011;

  @Test
  public void testHome() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = new TestRestTemplate()
        .getForEntity("http://localhost:" + this.port, Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void testHealth() throws Exception {
    ResponseEntity<String> entity = new TestRestTemplate().withBasicAuth("actuator", getPassword())
        .getForEntity("http://localhost:" + this.managementPort + "/admin/application/health",
            String.class);
    System.out.println(entity.getBody());
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()).contains("\"status\":\"UP\"");
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
