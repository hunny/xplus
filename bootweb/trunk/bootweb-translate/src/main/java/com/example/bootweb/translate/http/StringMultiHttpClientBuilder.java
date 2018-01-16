package com.example.bootweb.translate.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import com.example.bootweb.translate.api.HttpBuilder;
import com.example.bootweb.translate.api.Param;
import com.example.bootweb.translate.api.Parser;

public class StringMultiHttpClientBuilder implements HttpBuilder<String[], String[]> {

  private final List<Param> params = new ArrayList<>();
  private final List<Header> headers = new ArrayList<>();
  private Parser<String, String> parser = null;
  private URI uri = null;

  public static StringMultiHttpClientBuilder newBuilder() {
    return new StringMultiHttpClientBuilder();
  }

  @Override
  public HttpBuilder<String[], String[]> params(List<Param> params) {
    if (null != params) {
      this.params.addAll(params);
    }
    return this;
  }

  @Override
  public <P extends Parser> HttpBuilder<String[], String[]> parser(P parser) {
    this.parser = parser;
    return this;
  }

  @Override
  public HttpBuilder<String[], String[]> addHeader(String key, String name) {
    Assert.notNull(key, "key");
    Assert.notNull(name, "name");
    this.headers.add(new BasicHeader(key, name));
    return this;
  }
  
  public HttpBuilder<String[], String[]> addHeader(Header header) {
    Assert.notNull(header, "header");
    this.headers.add(header);
    return this;
  }

  @Override
  public HttpBuilder<String[], String[]> uri(String uri) {
    try {
      this.uri = new URI(uri);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
    return this;
  }

  @Override
  public String [] build() {
    Assert.notNull(uri, "uri");

    CloseableHttpClient httpclient = HttpClients.custom() //
        .build();
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    try {
      RequestBuilder builder = RequestBuilder //
          .post() //
          .setUri(uri); //
      for (Header header : headers) {
        builder.setHeader(header);
      }
      for (Param param : params) {
        builder.addParameter(param.getKey(), param.getValue());
      }
      HttpUriRequest request = builder.build();
      response = httpclient.execute(request);
      StatusLine statusLine = response.getStatusLine();
      if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
        entity = response.getEntity();
        String respstr = null == entity ? "" : EntityUtils.toString(entity);
        /*if (null != parser) {
          return parser.parse(respstr);
        }
        return respstr;*/
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    } finally {
      closeQuiet(httpclient, response, entity);
    }
    return null;
  }

  protected void closeQuiet(CloseableHttpClient httpclient, //
      CloseableHttpResponse response, //
      HttpEntity entity) {
    try {
      EntityUtils.consume(entity);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    try {
      response.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      httpclient.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
