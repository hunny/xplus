package com.example.bootweb.httpclient.quick.example;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * A simple example that uses HttpClient to execute an HTTP request over a
 * secure connection tunneled through an authenticating proxy.
 */
public class ClientProxyAuthentication {

  public static void main(String[] args) throws Exception {
    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(new AuthScope("localhost", 8087),
        new UsernamePasswordCredentials("squid", "squid"));
    credsProvider.setCredentials(new AuthScope("httpbin.org", 80),
        new UsernamePasswordCredentials("user", "passwd"));
    CloseableHttpClient httpclient = HttpClients.custom()
        .setDefaultCredentialsProvider(credsProvider).build();
    try {
      HttpHost target = new HttpHost("httpbin.org", 80, "http");
      HttpHost proxy = new HttpHost("localhost", 8087);

      RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
      HttpGet httpget = new HttpGet("/basic-auth/user/passwd");
      httpget.setConfig(config);

      System.out.println(
          "Executing request " + httpget.getRequestLine() + " to " + target + " via " + proxy);

      CloseableHttpResponse response = httpclient.execute(target, httpget);
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
