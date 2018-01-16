package com.example.bootweb.translate.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
  private List<URI> uris = new ArrayList<>();

  public static StringMultiHttpClientBuilder newBuilder() {
    return new StringMultiHttpClientBuilder();
  }

  @Override
  public HttpBuilder<String[], String[]> params(List<Param> params) {// TODO 待解决每个请求的参数问题。
    if (null != params) {
      this.params.addAll(params);
    }
    return this;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
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
      this.uris.add(new URI(uri));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
    return this;
  }

  @Override
  public String[] build() {
    Assert.notEmpty(this.uris, "uris");

    int number = 20;
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(number);
    CloseableHttpClient httpclient = HttpClients.custom() //
        .setConnectionManager(cm) //
        .build();
    try {
      List<String> result = multiBuild(number, httpclient);
      return result.toArray(new String[result.size()]);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      closeClientQuiet(httpclient);
    }
  }

  protected List<String> multiBuild(int number, CloseableHttpClient httpclient) {
    ExecutorService executorService = Executors.newFixedThreadPool(number);
    List<String> result = new ArrayList<>();
    for (final URI uri : uris) {
      Future<String> future = executorService.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
          return multiReq(httpclient, uri);
        }
      });
      try {
        result.add(future.get());
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
    return result;
  }

  protected String multiReq(CloseableHttpClient httpclient, URI uri) {
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
        if (null != parser) {
          return parser.parse(respstr);
        }
        return respstr;
      }
      throw new IllegalArgumentException("请求异常。");
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    } finally {
      closeQuiet(response, entity);
    }
  }

  protected void closeQuiet(CloseableHttpResponse response, //
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
  }

  private void closeClientQuiet(CloseableHttpClient httpclient) {
    try {
      httpclient.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
