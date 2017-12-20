package com.example.bootweb.accessory.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：HttpClient代理
 */
@Configuration
public class ProxyRouteConfig {

  // 代理的host地址
  @Value("${httpclient.config.proxyhost}")
  private String proxyHost;

  // 代理的端口号
  @Value("${httpclient.config.proxyPort}")
  private int proxyPort = 80;
  
  // 代理的端口号
  @Value("${httpclient.config.proxy:false}")
  private boolean proxy;

  @Bean
  public DefaultProxyRoutePlanner defaultProxyRoutePlanner() {
    if (!proxy) {
      return null;
    }
    HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort);
    return new DefaultProxyRoutePlanner(proxy);
  }

}
