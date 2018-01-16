package com.example.bootweb.translate.google;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.example.bootweb.translate.api.HttpBuilder;
import com.example.bootweb.translate.api.Lang;
import com.example.bootweb.translate.api.Param;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.api.TranslateBuilder;
import com.example.bootweb.translate.http.StringHttpClientBuilder;
import com.example.bootweb.translate.http.UserAgent;

public class GoogleHybirdTranslateBuilder implements TranslateBuilder<InputStream, OutputStream> {

  private HttpBuilder<String, String> httpBuilder = null;
  private InputStream inputStream = null;
  private Class<? extends Lang> from = null;
  private Class<? extends Lang> to = null;
  private long start = 0;

  public static GoogleHybirdTranslateBuilder newBuilder() {
    return new GoogleHybirdTranslateBuilder();
  }

  @Override
  public OutputStream build() {
    Assert.notNull(from, "from");
    Assert.notNull(to, "to");
    Assert.notNull(inputStream, "inputStream");

    try {
      return translate();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  protected OutputStream translate() throws IOException {
    OutputStream outputStream = new ByteArrayOutputStream();

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    BufferedWriter br = new BufferedWriter(new OutputStreamWriter(outputStream));
    String line = null;
    while ((line = reader.readLine()) != null) {
      if (StringUtils.isEmpty(line)) {
        continue;
      }
      Translate translate = new Translate(from, to, line);
      act(translate);
      br.write(line);
      br.write(translate.getTarget());
      br.flush();
    }
    closeQuiet(reader, br);
    return outputStream;
  }

  protected void closeQuiet(BufferedReader reader, BufferedWriter br) throws IOException {
    br.close();
    reader.close();
  }

  protected void act(Translate translate) {
    List<Param> params = GoogleParamsBuilder //
        .newBuilder(translate.getFrom(), //
            translate.getTo()) //
        .setText(translate.getText()) //
        .build();//

    getHttpBuilder() //
        .uri(Googles.TRANSLATE_URL) //
        .params(params) //
        .addHeader(HttpBuilder.REFERER, //
            Googles.TRANSLATE_REFERER) //
        .addHeader(HttpBuilder.USER_AGENT, //
            UserAgent.get()) //
        .parser(GoogleResultParser.newParser()) //
    ;//
    translate.setTarget(httpBuilder.build());
  }

  @Override
  public GoogleHybirdTranslateBuilder from(Class<? extends Lang> from) {
    this.from = from;
    return this;
  }

  @Override
  public GoogleHybirdTranslateBuilder to(Class<? extends Lang> to) {
    this.to = to;
    return this;
  }

  @Override
  public GoogleHybirdTranslateBuilder source(InputStream inputStream) {
    this.inputStream = inputStream;
    return this;
  }

  public GoogleHybirdTranslateBuilder start(long start) {
    this.start = start;
    return this;
  }

  public GoogleHybirdTranslateBuilder httpBuilder(HttpBuilder<String, String> httpBuilder) {
    this.httpBuilder = httpBuilder;
    return this;
  }

  protected HttpBuilder<String, String> getHttpBuilder() {
    if (null == httpBuilder) {// Default Http Builder
      httpBuilder = StringHttpClientBuilder //
          .newBuilder(); //
    }
    return httpBuilder;
  }

}
