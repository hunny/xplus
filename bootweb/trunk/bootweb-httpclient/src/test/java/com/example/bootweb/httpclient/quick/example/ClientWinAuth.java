package com.example.bootweb.httpclient.quick.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates how to create HttpClient pre-configured with
 * support for integrated Windows authentication.
 */
public class ClientWinAuth {

  public final static void main(String[] args) throws Exception {

    if (!WinHttpClients.isWinAuthAvailable()) {
      System.out.println("Integrated Win auth is not supported!!!");
    }

    CloseableHttpClient httpclient = WinHttpClients.createDefault();
    // There is no need to provide user credentials
    // HttpClient will attempt to access current user security context through
    // Windows platform specific methods via JNI.
    try {
      HttpGet httpget = new HttpGet("http://winhost/");

      System.out.println("Executing request " + httpget.getRequestLine());
      CloseableHttpResponse response = httpclient.execute(httpget);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        EntityUtils.consume(response.getEntity());
      } finally {
        response.close();
      }
    } finally {
      httpclient.close();
    }
  }

}
