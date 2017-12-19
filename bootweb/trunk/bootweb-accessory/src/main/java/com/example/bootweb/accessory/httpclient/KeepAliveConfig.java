package com.example.bootweb.accessory.httpclient;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：连接保持策略
 */
@Configuration
public class KeepAliveConfig {

  @Value("${httpclient.config.keepAliveTime}")
  private int keepAliveTime = 30;

  @Bean("connectionKeepAliveStrategy")
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
    return new ConnectionKeepAliveStrategy() {

      public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        // Honor 'keep-alive' header
        HeaderElementIterator it = new BasicHeaderElementIterator(
            response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
          HeaderElement he = it.nextElement();
          String param = he.getName();
          String value = he.getValue();
          if (value != null && param.equalsIgnoreCase("timeout")) {
            try {
              return Long.parseLong(value) * 1000;
            } catch (NumberFormatException ignore) {
            }
          }
        }
        return 30 * 1000;
      }
    };
  }
}
