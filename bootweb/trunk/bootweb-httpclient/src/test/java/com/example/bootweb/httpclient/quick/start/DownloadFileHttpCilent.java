package com.example.bootweb.httpclient.quick.start;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class DownloadFileHttpCilent {

  public static void main(String[] args) throws Exception {
    DownloadFileHttpCilent client = new DownloadFileHttpCilent();
    client.download(args);
  }

  public void download(String [] args) {
    try {
      CloseableHttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(args[0]);
      HttpResponse response = client.execute(request);
      HttpEntity entity = response.getEntity();
      int responseCode = response.getStatusLine().getStatusCode();
      System.out.println("Request Url: " + request.getURI());
      System.out.println("Response Code: " + responseCode);
      InputStream is = entity.getContent();
      String filePath = args[1];
      FileOutputStream fos = new FileOutputStream(new File(filePath));

      int inByte;
      while ((inByte = is.read()) != -1) {
        fos.write(inByte);
      }

      is.close();
      fos.close();

      client.close();
      System.out.println("File Download Completed!!!");
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
