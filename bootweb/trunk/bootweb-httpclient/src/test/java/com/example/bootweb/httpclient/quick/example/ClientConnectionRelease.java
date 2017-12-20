package com.example.bootweb.httpclient.quick.example;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.example.bootweb.httpclient.quick.Constants;

/**
 * This example demonstrates the recommended way of using API to make sure the
 * underlying connection gets released back to the connection manager.
 * 
 * This example demonstrates how to ensure the release of the underlying HTTP
 * connection back to the connection manager in case of a manual processing of
 * HTTP responses.
 */
public class ClientConnectionRelease {

  public final static void main(String[] args) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpGet httpget = new HttpGet(Constants.URL_ABOUT);

      System.out.println("Executing request " + httpget.getRequestLine());
      CloseableHttpResponse response = httpclient.execute(httpget);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());

        // Get hold of the response entity
        HttpEntity entity = response.getEntity();

        // If the response does not enclose an entity, there is no need
        // to bother about connection release
        if (entity != null) {
          InputStream instream = entity.getContent();
          try {
            instream.read();
            // do something useful with the response
          } catch (IOException ex) {
            // In case of an IOException the connection will be released
            // back to the connection manager automatically
            throw ex;
          } finally {
            // Closing the input stream will trigger connection release
            instream.close();
          }
        }
      } finally {
        response.close();
      }
    } finally {
      httpclient.close();
    }
  }

}
