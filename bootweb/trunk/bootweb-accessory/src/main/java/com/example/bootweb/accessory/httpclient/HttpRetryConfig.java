package com.example.bootweb.accessory.httpclient;

import org.apache.http.client.HttpRequestRetryHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HttpClient的重试处理配置
 */
@Configuration
public class HttpRetryConfig {

  @Value("${httpclient.config.retryTime}") // 此处建议采用@ConfigurationProperties(prefix="httpclient.config")方式，方便复用
  private int retryTime;

  @Bean
  public HttpRequestRetryHandler httpRequestRetryHandler() {
    return new HttpRetryHandler(retryTime);
  }
}
