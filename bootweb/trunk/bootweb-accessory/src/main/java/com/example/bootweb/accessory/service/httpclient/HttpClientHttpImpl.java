package com.example.bootweb.accessory.service.httpclient;

import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bootweb.accessory.api.Http;

@Service
public class HttpClientHttpImpl implements Http<String> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Optional<CloseableHttpClient> httpClientService;

  @Override
  public String get(String url) {
    if (!httpClientService.isPresent()) {
      throw new IllegalArgumentException("httpClient is null");
    }
    CloseableHttpClient httpClient = httpClientService.get();
    try {
      return httpGet(httpClient, url);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private String httpGet(CloseableHttpClient httpClient, String url) throws Exception {
    HttpGet httpGet = new HttpGet(url);
    httpGet.setHeader("Accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
    httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
    httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6");
    httpGet.setHeader("Connection", "keep-alive");
    // httpGet.setHeader("Host", "m.tianyancha.com");
    // httpGet.setHeader("Upgrade-Insecure-Requests", "1");
    httpGet.setHeader("User-Agent",
        "ozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36");

    CloseableHttpResponse resp = httpClient.execute(httpGet);
    logger.info("Status Code [{}], URL[{}]", resp.getStatusLine(), url);
    HttpEntity entity = resp.getEntity();
    String str = EntityUtils.toString(entity);
    logger.info("Response[{}]", str);
    EntityUtils.consume(entity);
    return str;
  }

}
