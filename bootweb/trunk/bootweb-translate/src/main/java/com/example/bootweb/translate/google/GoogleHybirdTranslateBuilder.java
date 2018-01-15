package com.example.bootweb.translate.google;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.util.Assert;

import com.example.bootweb.translate.api.HttpBuilder;
import com.example.bootweb.translate.api.Lang;
import com.example.bootweb.translate.api.Param;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.api.TranslateBuilder;
import com.example.bootweb.translate.http.StringHttpClientBuilder;
import com.example.bootweb.translate.http.UserAgent;

public class GoogleHybirdTranslateBuilder implements TranslateBuilder<InputStream, OutputStream> {

  private Translate translate;
  private HttpBuilder<String, String> httpBuilder;
  private InputStream inputStream;

  public static GoogleHybirdTranslateBuilder newBuilder() {
    return new GoogleHybirdTranslateBuilder();
  }

  @Override
  public OutputStream build() {
    Assert.notNull(translate, "translate");

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
//    return translate;
    return null;
  }

  @Override
  public GoogleHybirdTranslateBuilder from(Class<? extends Lang> from) {
    getTranslate().setFrom(from);
    return this;
  }

  @Override
  public GoogleHybirdTranslateBuilder to(Class<? extends Lang> to) {
    getTranslate().setTo(to);
    return this;
  }

  @Override
  public GoogleHybirdTranslateBuilder source(InputStream inputStream) {
    this.inputStream = inputStream;
    return this;
  }

  public GoogleHybirdTranslateBuilder httpBuilder(HttpBuilder<String, String> httpBuilder) {
    this.httpBuilder = httpBuilder;
    return this;
  }

  protected Translate getTranslate() {
    if (null == translate) { // Default Translate
      translate = new Translate();
    }
    return translate;
  }

  protected HttpBuilder<String, String> getHttpBuilder() {
    if (null == httpBuilder) {// Default Http Builder
      httpBuilder = StringHttpClientBuilder //
          .newBuilder(); //
    }
    return httpBuilder;
  }

}
