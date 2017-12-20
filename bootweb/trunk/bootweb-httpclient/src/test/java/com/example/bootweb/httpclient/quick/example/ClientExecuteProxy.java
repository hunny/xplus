package com.example.bootweb.httpclient.quick.example;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * How to send a request via proxy.
 *
 * @since 4.0
 */
public class ClientExecuteProxy {

  public static void main(String[] args) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpHost target = new HttpHost("localhost", 8081, "http");
      HttpHost proxy = new HttpHost("127.0.0.1", 8087, "http");

      RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
      HttpGet request = new HttpGet("/about");
      request.setConfig(config);

      System.out.println(
          "Executing request " + request.getRequestLine() + " to " + target + " via " + proxy);

      CloseableHttpResponse response = httpclient.execute(target, request);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
      } finally {
        response.close();
      }
    } finally {
      httpclient.close();
    }
  }

}
