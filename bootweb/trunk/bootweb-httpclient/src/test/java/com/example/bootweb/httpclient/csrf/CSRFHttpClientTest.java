package com.example.bootweb.httpclient.csrf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class CSRFHttpClientTest {

  public void run() throws Exception {

    HttpClientContext context = HttpClientContext.create();
    CookieStore cookieStore = new BasicCookieStore();
    context.setCookieStore(cookieStore);

    HttpGet get = new HttpGet("https://example.com/");
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = httpClient.execute(get, context);

    HttpPost post = new HttpPost("https://example.com/potentially/harmful/path");
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("_csrf", getCookieValue(cookieStore, "_csrf")));
    // Add any other needed post parameters
    UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
    post.setEntity(paramEntity);
    // Make sure cookie headers are written
    RequestAddCookies addCookies = new RequestAddCookies();
    addCookies.process(post, context);

    response = httpClient.execute(get, context);

    System.out.println("Response HTTP Status Code:" + response.getStatusLine().getStatusCode());
    System.out.println(readResponse(response));
  }

  public String readResponse(CloseableHttpResponse response) throws Exception {
    BufferedReader reader = null;
    String content = "";
    String line = null;
    HttpEntity entity = response.getEntity();

    reader = new BufferedReader(new InputStreamReader(entity.getContent()));
    while ((line = reader.readLine()) != null) {
      content += line;
    }
    // ensure response is fully consumed
    EntityUtils.consume(entity);
    return content;
  }

  public String getCookieValue(CookieStore cookieStore, String cookieName) {
    String value = null;
    for (Cookie cookie : cookieStore.getCookies()) {
      if (cookie.getName().equals(cookieName)) {
        value = cookie.getValue();
      }
    }
    return value;
  }

  public static final void main(final String[] args) throws Exception {
    CSRFHttpClientTest x = new CSRFHttpClientTest();
    x.run();
  }
}
