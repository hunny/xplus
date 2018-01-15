package com.example.bootweb.translate.google;

import java.util.List;

import org.springframework.util.Assert;

import com.example.bootweb.translate.api.HttpBuilder;
import com.example.bootweb.translate.api.Lang;
import com.example.bootweb.translate.api.Param;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.api.TranslateBuilder;
import com.example.bootweb.translate.http.StringHttpClientBuilder;
import com.example.bootweb.translate.http.UserAgent;

public class GoogleTranslateBuilder implements TranslateBuilder<String, Translate> {

  private Translate translate;
  private HttpBuilder<String, String> httpBuilder;

  public static GoogleTranslateBuilder newBuilder() {
    return new GoogleTranslateBuilder();
  }

  public GoogleTranslateBuilder setTranslate(Translate translate) {
    this.translate = translate;
    return this;
  }

  @Override
  public Translate build() {
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

  public GoogleTranslateBuilder httpBuilder(HttpBuilder<String, String> httpBuilder) {
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
