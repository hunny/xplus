package com.example.bootweb.accessory.builder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.accessory.api.HttpBuilder;
import com.example.bootweb.accessory.api.Param;
import com.example.bootweb.accessory.api.Parser;

public class StringHttpBuilder implements HttpBuilder<List<Param>, String> {

  private final Logger logger = LoggerFactory.getLogger(StringHttpBuilder.class);
  
  private String uri = null;
  private final List<Param> params = new ArrayList<>();
  private final List<Param> headers = new ArrayList<>();
  private HttpClient httpClient;

  public static StringHttpBuilder newBuilder() {
    return new StringHttpBuilder();
  }

  @Override
  public HttpBuilder<List<Param>, String> uri(String uri) {
    this.uri = uri;
    return this;
  }

  @Override
  public HttpBuilder<List<Param>, String> params(List<Param> params) {
    if (null != params) {
      this.params.clear();
      this.params.addAll(params);
    }
    return this;
  }

  @Override
  public HttpBuilder<List<Param>, String> header(List<Param> params) {
    if (null != params) {
      this.headers.clear();
      this.headers.addAll(params);
    }
    return this;
  }

  @Override
  public HttpBuilder<List<Param>, String> parser(Parser parser) {
    return this;
  }

  @Override
  public HttpBuilder<List<Param>, String> method(String method) {
    return this;
  }

  public HttpBuilder<List<Param>, String> httpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
    return this;
  }

  @Override
  public String build() {
    StringBuilder url = new StringBuilder();
    url.append(uri);
    url.append(params.isEmpty() ? "": "?");
    for (Param param : params) {
      url.append(param.getKey());
      url.append("=");
      url.append(encode(param.getValue()));
      url.append("&");
    }
    HttpGet httpGet = new HttpGet(url.toString());
    for (Param header : headers) {
      httpGet.setHeader(header.getKey(), header.getValue());
    }
    try {
      HttpResponse resp = httpClient.execute(httpGet);
      logger.info("Status Code [{}], URL[{}]", resp.getStatusLine(), uri);
      HttpEntity entity = resp.getEntity();
      String str = EntityUtils.toString(entity);
      logger.info("Response[{}]", str);
      EntityUtils.consume(entity);
      return str;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("static-method")
  private String encode(String value) {
    try {
      return URLEncoder.encode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

}
