package com.example.bootweb.httpclient.quick.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.example.bootweb.httpclient.quick.Constants;

/**
 * This example demonstrates how to abort an HTTP method before its normal
 * completion.
 * 
 * This example demonstrates how to abort an HTTP request before its normal
 * completion.
 */
public class ClientAbortMethod {

  public final static void main(String[] args) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpGet httpget = new HttpGet(Constants.URL_ABOUT);

      System.out.println("Executing request " + httpget.getURI());
      CloseableHttpResponse response = httpclient.execute(httpget);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        // Do not feel like reading the response body
        // Call abort on the request object
        httpget.abort();
      } finally {
        response.close();
      }
    } finally {
      httpclient.close();
    }
  }

}
