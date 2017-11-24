package com.example.bootweb.pg.config;

import java.util.Collections;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.example.bootweb.pg.logging.LoggingRestTemplate;

@Configuration
public class RestClientConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    RestTemplate restTemplate = builder.build();
    restTemplate.setInterceptors(Collections.singletonList(new LoggingRestTemplate()));
    restTemplate.setRequestFactory(new HttpComponentsAsyncClientHttpRequestFactory());
    return restTemplate;
  }

}
