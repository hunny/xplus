package com.example.bootweb.httpclient.quick.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Download {

  public static final String URL = "http://v3-dy.ixigua.com/b0b797a73bb720c8496902ce285ce1f1/5ad0d415/video/m/220333f229cc84d43c7baed232054eb772d115582fc0000b2e755a6b1cf/#mp4";

  public static void main(String[] args) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // http://service.iiilab.com/video/douyin
    
    HttpPost httpPost = new HttpPost("http://service.iiilab.com/video/douyin");
    httpPost.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
    httpPost.addHeader("Accept-Encoding", "gzip, deflate");
    httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6");
    httpPost.addHeader("Connection", "keep-alive");
    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    httpPost.addHeader("Host", "service.iiilab.com");
    httpPost.addHeader("Origin", "http://douyin.iiilab.com");
    httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    nvps.add(new BasicNameValuePair("link", "https://www.iesdouyin.com/share/video/6543470964556958989/?region=CN"));
    nvps.add(new BasicNameValuePair("r", "2945972395592975"));//2596429517981844
    nvps.add(new BasicNameValuePair("s", "1990133558"));// 484300877
    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
    CloseableHttpResponse postResponse = httpclient.execute(httpPost);
    try {
      System.out.println(postResponse.getStatusLine());
      System.out.println("POST返回状态码" + postResponse.getStatusLine().getStatusCode());
      System.out.println(readResponse(postResponse));
    } finally {
      postResponse.close();
    }
    
//    downloadmp4(httpclient);
    httpclient.close();
    System.out.println("File Download Completed!!!");
  }
  
  public static String readResponse(CloseableHttpResponse response) throws Exception {
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

  protected static void downloadmp4(CloseableHttpClient httpclient)
      throws IOException, ClientProtocolException, FileNotFoundException {
    HttpGet request = new HttpGet(URL);
    request.setHeader("Accept-Type", "application/octet-stream");
    CloseableHttpResponse response = httpclient.execute(request);
    HttpEntity entity = response.getEntity();
    int responseCode = response.getStatusLine().getStatusCode();
    System.out.println("Request Url: " + request.getURI());
    System.out.println("Response Code: " + responseCode);
    InputStream is = entity.getContent();
    String filePath = "/Users/hunnyhu/Downloads/abc.mp4";
    FileOutputStream fos = new FileOutputStream(new File(filePath));
    int inByte;
    while ((inByte = is.read()) != -1) {
      fos.write(inByte);
    }
    is.close();
    fos.close();
  }

}
