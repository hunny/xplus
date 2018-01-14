package com.example.bootweb.translate.google;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.api.Params;
import com.example.bootweb.translate.profile.TranslateBasicDemo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * https://www.cnblogs.com/wcymiss/p/6264847.html
 */
@Component
@TranslateBasicDemo
public class TranslateBasicDemoRunner implements CommandLineRunner {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void run(String... args) throws Exception {
    List<Params> params = GoogleParamsBuilder //
        .newBuilder(EN.class, CN.class) //
        .setText(
            "may be included more than once and specifies what to return in the reply.Here are some values for dt. If the value is set, the following data will be returned:") //
        .build();//
    for (Params param : params) {
      System.out.println(param);
    }
    CloseableHttpClient httpclient = HttpClients.custom() //
        .build();

    RequestBuilder builder = RequestBuilder.post() //
        .setHeader(HttpHeaders.REFERER, //
            "http://translate.google.cn/") //
        .setHeader(HttpHeaders.USER_AGENT, //
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
        .setUri(new URI("http://translate.google.cn/translate_a/single")); //
    for (Params param : params) {
      builder.addParameter(param.getKey(), param.getValue());
    }
    HttpUriRequest translate = builder.build();
    CloseableHttpResponse response = httpclient.execute(translate);
    HttpEntity entity = null;
    try {
      entity = response.getEntity();
      System.out.println("Translate form get: " + response.getStatusLine());
      if (entity != null) {
        System.out.println("Response content length: " + entity.getContentLength());
        String respstr = EntityUtils.toString(entity);
        System.out.println("Response content : " + parseString(respstr));
      }
    } finally {
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
    System.exit(0);
  }

  @SuppressWarnings("unchecked")
  protected String parseString(String jsonString) {
    try {
      List<Object> values = objectMapper.readValue(jsonString, new TypeReference<List<Object>>() {
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
    return "";
  }

}
