package com.example.bootweb.httpclient.quick.start;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickStartTest {

  public static final String BASE_URL = "http://localhost:8081/";

  @Test
  public void testNativeAPI() throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(BASE_URL + "about");
    CloseableHttpResponse getResponse = httpclient.execute(httpGet);
    // The underlying HTTP connection is still held by the response object
    // to allow the response content to be streamed directly from the network
    // socket.
    // In order to ensure correct deallocation of system resources
    // the user MUST call CloseableHttpResponse#close() from a finally clause.
    // Please note that if response content is not fully consumed the underlying
    // connection cannot be safely re-used and will be shut down and discarded
    // by the connection manager.
    try {
      System.out.println(getResponse.getStatusLine());
      Assert.assertEquals("GET返回状态码相同", HttpStatus.OK.value(),
          getResponse.getStatusLine().getStatusCode());
      HttpEntity entity1 = getResponse.getEntity();
      // do something useful with the response body
      // and ensure it is fully consumed
      EntityUtils.consume(entity1);
    } finally {
      getResponse.close();
    }

    HttpPost httpPost = new HttpPost(BASE_URL + "login");
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    nvps.add(new BasicNameValuePair("username", "vip"));
    nvps.add(new BasicNameValuePair("password", "secret"));
    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
    CloseableHttpResponse postResponse = httpclient.execute(httpPost);

    try {
      System.out.println(postResponse.getStatusLine());
      Assert.assertEquals("POST返回状态码相同", HttpStatus.OK.value(),
          postResponse.getStatusLine().getStatusCode());
      HttpEntity postEntity = postResponse.getEntity();
      // do something useful with the response body
      // and ensure it is fully consumed
      EntityUtils.consume(postEntity);
    } finally {
      postResponse.close();
    }
  }

  @Test
  public void testFluentAPI() throws Exception {
    // The fluent API relieves the user from having to deal with manual
    // deallocation of system
    // resources at the cost of having to buffer response content in memory in
    // some cases.

    Content getReturnContent = Request.Get(BASE_URL + "about") //
        .execute() //
        .returnContent(); //
    System.out.println(getReturnContent);
    Content postReturnContent = Request.Post(BASE_URL + "login") //
        .bodyForm( //
            Form.form() //
                .add("username", "vip") //
                .add("password", "secret") //
                .build()) //
        .execute() //
        .returnContent(); //
    System.out.println(postReturnContent);
    System.out.println("TestFluentAPI over.");
  }

}
