package com.example.bootweb.actuator;

import java.util.Arrays;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.EndpointProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springboot.actuator.Application;

/**
 * Basic integration tests for service demo application.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, //
    webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class SampleActuatorApplicationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testHomeIsSecure() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("/", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("error")).isEqualTo("Unauthorized");
    Assertions.assertThat(entity.getHeaders()).doesNotContainKey("Set-Cookie");
  }

  @Test
  public void testMetricsIsSecure() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.getForEntity("/metrics", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    entity = this.restTemplate.getForEntity("/metrics/", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    entity = this.restTemplate.getForEntity("/metrics/foo", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    entity = this.restTemplate.getForEntity("/metrics.json", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void testHome() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("user", getPassword()).getForEntity("/", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("message")).isEqualTo("Hello Hello");
  }

  @SuppressWarnings(value = { "unchecked", "rawtypes" })
  @Test
  public void testMetrics() throws Exception {
    testHome(); // makes sure some requests have been made
    ResponseEntity<Map> entity = this.restTemplate //
        .withBasicAuth("actuator", getPassword()) //
        .getForEntity("/metrics", Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.containsKey("mem"));
    Assertions.assertThat(body.containsKey("heap.used"));

  }

  @Test
  public void testEnv() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("actuator", getPassword()).getForEntity("/env",
        Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    System.out.println(body);
    Assertions.assertThat(body).containsKey("server.ports");
  }

  @Test
  public void testHealth() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.withBasicAuth("user", getPassword()).getForEntity("/health",
        String.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()).contains("\"status\":\"UP\"");
    Assertions.assertThat(entity.getBody()).doesNotContain("\"hello\":\"1\"");
  }

  @Test
  public void testInfo() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.withBasicAuth("user", getPassword()).getForEntity("/info",
        String.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()).contains("\"artifact\":\"example-actuator\"");
    Assertions.assertThat(entity.getBody()).contains("\"someKey\":\"someValue\"");
    // Assertions.assertThat(entity.getBody()).contains("\"java\":{",
    // "\"source\":\"1.8\"",
    // "\"target\":\"1.8\"");
    // Assertions.assertThat(entity.getBody()).contains("\"encoding\":{",
    // "\"source\":\"UTF-8\"",
    // "\"reporting\":\"UTF-8\"");
  }

  @Test
  public void testErrorPage() throws Exception {
    ResponseEntity<String> entity = this.restTemplate.withBasicAuth("user", getPassword()).getForEntity("/foo",
        String.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    String body = entity.getBody();
    Assertions.assertThat(body).contains("\"error\":");
  }

  @Test
  public void testHtmlErrorPage() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
    HttpEntity<?> request = new HttpEntity<Void>(headers);
    ResponseEntity<String> entity = this.restTemplate.withBasicAuth("user", getPassword()).exchange("/foo",
        HttpMethod.GET, request, String.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    String body = entity.getBody();
    Assertions.assertThat(body).as("Body was null").isNotNull();
    Assertions.assertThat(body).contains("This application has no explicit mapping for /error");
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testTrace() throws Exception {
    this.restTemplate.getForEntity("/health", String.class);
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map[]> entity = this.restTemplate.withBasicAuth("actuator", getPassword()).getForEntity("/trace",
        Map[].class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> body = entity.getBody()[0];
    Map<String, Object> map = (Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) body.get("info"))
        .get("headers")).get("response");
    Assertions.assertThat(map.get("status")).isEqualTo("200");
  }

  @Test
  @SuppressWarnings(value = { "unchecked", "rawtypes" })
  public void traceWithParameterMap() throws Exception {

    this.restTemplate.withBasicAuth("user", getPassword()).getForEntity("/health?param1=value1", String.class);

    ResponseEntity<Map[]> entity = this.restTemplate.withBasicAuth("actuator", getPassword()).getForEntity("/trace",
        Map[].class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> body = entity.getBody()[0];
    Map<String, Object> info = (Map<String, Object>) body.get("info");
    Map<String, Object> map = (Map<String, Object>) info.get("parameters");
    Assertions.assertThat(map.get("param1")).isNotNull();
  }

  @Test
  public void testErrorPageDirectAccess() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("user", getPassword()).getForEntity("/error",
        Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    @SuppressWarnings("unchecked")
    Map<String, Object> body = entity.getBody();
    Assertions.assertThat(body.get("error")).isEqualTo("None");
    Assertions.assertThat(body.get("status")).isEqualTo(999);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testBeans() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map[]> entity = this.restTemplate.withBasicAuth("actuator", getPassword()).getForEntity("/beans",
        Map[].class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(entity.getBody()[0]).containsKeys("beans", "context");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testConfigProps() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.restTemplate.withBasicAuth("actuator", getPassword()).getForEntity("/configprops",
        Map.class);
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<String, Object> body = entity.getBody();
    System.err.println(body);
    Assertions.assertThat(body.containsKey("endpoints-" + EndpointProperties.class.getName()));
  }

  @SuppressWarnings("static-method")
  private String getPassword() {
    return "password";
  }

}
