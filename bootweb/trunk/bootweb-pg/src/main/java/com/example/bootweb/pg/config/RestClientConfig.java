package com.example.bootweb.pg.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.example.bootweb.pg.logging.LoggingRestTemplate;

@Configuration
public class RestClientConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    RestTemplate restTemplate = builder.build();
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
    interceptors.add(new LoggingRestTemplate());
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }

}
