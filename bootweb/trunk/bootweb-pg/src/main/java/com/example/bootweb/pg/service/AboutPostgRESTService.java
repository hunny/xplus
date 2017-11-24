package com.example.bootweb.pg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.bootweb.pg.domain.About;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AboutPostgRESTService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${bootweb-pg.postgrest.server.url:http://localhost:30000}/")
  private String serverUrl;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  public Object[] getByPostgREST(String postgREST) {
    ResponseEntity<Object[]> responseEntity = //
        restTemplate.getForEntity(serverUrl + postgREST, Object[].class);
    MediaType contentType = responseEntity.getHeaders().getContentType();
    HttpStatus statusCode = responseEntity.getStatusCode();
    logger.info("contentType: {}, statusCode: {}", contentType, statusCode);
    return responseEntity.getBody();
  }

  public About create(About about) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>( //
        objectMapper.writeValueAsString(about), headers);
    ResponseEntity<Object> resp = restTemplate.exchange(serverUrl, //
        HttpMethod.POST, //
        entity, //
        Object.class);
    logger.info("response {}", resp);
    if (HttpStatus.CREATED == resp.getStatusCode()) {
      ResponseEntity<About[]> responseEntity = //
          restTemplate.getForEntity(
              serverUrl //
                  + resp.getHeaders().getLocation().toString(), //
              About[].class);
      About[] objects = responseEntity.getBody();
      if (objects.length > 0) {
        return objects[0];
      }
    }
    return null;
  }

  public About getById(Long id) {
    ResponseEntity<About[]> responseEntity = //
        restTemplate.getForEntity(serverUrl + "about?id=eq." + id, About[].class);
    About[] objects = responseEntity.getBody();
    MediaType contentType = responseEntity.getHeaders().getContentType();
    HttpStatus statusCode = responseEntity.getStatusCode();
    logger.info("contentType: {}, statusCode: {}", contentType, statusCode);
    if (objects.length == 0) {
      return null;
    }
    return objects[0];
  }

}
