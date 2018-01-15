package com.example.bootweb.translate.google;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import com.example.bootweb.translate.api.Lang;
import com.example.bootweb.translate.api.Params;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.api.TranslateBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleTranslateBuilder implements TranslateBuilder<String, Translate> {

  private Translate translate;
  private ObjectMapper objectMapper;

  public static GoogleTranslateBuilder newBuilder() {
    return new GoogleTranslateBuilder();
  }

  public GoogleTranslateBuilder setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return this;
  }

  public GoogleTranslateBuilder setTranslate(Translate translate) {
    this.translate = translate;
    return this;
  }

  @Override
  public Translate build() {
    Assert.notNull(translate, "translate");
    if (null == objectMapper) {
      objectMapper = getDefaultObjectMapper();
    }
    List<Params> params = GoogleParamsBuilder //
        .newBuilder(translate.getFrom(), //
            translate.getTo()) //
        .setText(translate.getText()) //
        .build();//
    CloseableHttpClient httpclient = HttpClients.custom() //
        .build();
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    try {
      RequestBuilder builder = RequestBuilder.post() //
          .setHeader(HttpHeaders.REFERER, //
              "http://translate.google.cn/") //
          .setHeader(HttpHeaders.USER_AGENT, //
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
          .setUri(new URI("http://translate.google.cn/translate_a/single")); //
      for (Params param : params) {
        builder.addParameter(param.getKey(), param.getValue());
      }
      HttpUriRequest request = builder.build();
      response = httpclient.execute(request);
      StatusLine statusLine = response.getStatusLine();
      if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
        entity = response.getEntity();
        String respstr = null == entity ? "" : EntityUtils.toString(entity);
        translate.setTarget(parseResult(respstr));
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    } finally {
      closeQuiet(httpclient, response, entity);
    }
    return translate;
  }

  @Override
  public GoogleTranslateBuilder from(Class<? extends Lang> from) {
    getTranslate().setFrom(from);
    return this;
  }

  @Override
  public GoogleTranslateBuilder to(Class<? extends Lang> to) {
    getTranslate().setTo(to);
    return this;
  }

  @Override
  public GoogleTranslateBuilder source(String text) {
    getTranslate().setText(text);
    return this;
  }

  protected String parseResult(String result) {
    try {
      List<Object> values = objectMapper.readValue(result, new TypeReference<List<Object>>() {
      });
      if (null != values && values.size() > 0 && values.get(0) instanceof List) {
        List<List<String>> values2 = (List<List<String>>) values.get(0);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values2.size(); i++) {
          List<String> v = values2.get(i);
          if (null != v.get(0)) {
            buffer.append(v.get(0));
          }
        }
        return buffer.toString();
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return null;
  }

  protected Translate getTranslate() {
    if (null == translate) {
      translate = new Translate();
    }
    return translate;
  }

  protected ObjectMapper getDefaultObjectMapper() {
    return new ObjectMapper();
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
