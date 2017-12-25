package com.example.bootweb.jersey;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JerseyApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void contextLoads() {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/rs/hello", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void reverse() {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/rs/reverse?input=olleh",
        String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody()).isEqualTo("hello");
  }

  @Test
  public void validation() {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/rs/reverse", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void actuatorStatus() {
    ResponseEntity<String> entity = this.restTemplate.getForEntity("/health",
        String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody()).isEqualTo("{\"status\":\"UP\"}");
  }

}
