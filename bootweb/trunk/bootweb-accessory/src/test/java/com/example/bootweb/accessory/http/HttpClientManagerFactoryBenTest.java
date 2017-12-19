package com.example.bootweb.accessory.http;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpClientManagerFactoryBenTest {
  
  // 注入HttpClient实例
  @Resource(name = "httpClient")
  private CloseableHttpClient client;

  @Test
  public void test() throws Exception {
    int count = 10;
    final CountDownLatch latch = new CountDownLatch(count);
    ExecutorService service = Executors.newFixedThreadPool(2);
    for (int i = 0; i < count; i++) {
      service.submit(new Runnable() {
        @Override
        public void run() {
          System.out.println("the current thread is:" + Thread.currentThread().getName());
          HttpEntity entity = null;
          try {
            HttpGet get = new HttpGet("http://localhost:8080/about");
            // 通过httpclient的execute提交 请求 ，并用CloseableHttpResponse接受返回信息
            CloseableHttpResponse response = client.execute(get);
            System.out.println("client object:" + client);
            entity = response.getEntity();
            System.out.println(
                "============" + EntityUtils.toString(entity, Consts.UTF_8) + "=============");
            EntityUtils.consumeQuietly(entity);// 释放连接
          } catch (ClientProtocolException e) {
            e.printStackTrace();
          } catch (ParseException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            if (null != entity) {// 释放连接
              EntityUtils.consumeQuietly(entity);
            }
            latch.countDown();
          }
        }
      });
    }
    try {
      latch.await();
      System.out.println("execute over.");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e);
    }
  }
}
