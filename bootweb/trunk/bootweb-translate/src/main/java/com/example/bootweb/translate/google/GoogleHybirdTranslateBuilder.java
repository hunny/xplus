package com.example.bootweb.translate.google;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.example.bootweb.translate.api.HttpBuilder;
import com.example.bootweb.translate.api.Lang;
import com.example.bootweb.translate.api.Param;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.api.TranslateBuilder;
import com.example.bootweb.translate.http.StringHttpClientBuilder;
import com.example.bootweb.translate.http.UserAgent;

public class GoogleHybirdTranslateBuilder implements TranslateBuilder<InputStream, Void> {

  private final Logger logger = LoggerFactory.getLogger(GoogleHybirdTranslateBuilder.class);
  
  private InputStream inputStream = null;
  private OutputStream outputStream = null;
  private Class<? extends Lang> from = null;
  private Class<? extends Lang> to = null;
  private int sleep = 0;
  private long start = 0;

  public static GoogleHybirdTranslateBuilder newBuilder() {
    return new GoogleHybirdTranslateBuilder();
  }

  @Override
  public Void build() {
    Assert.notNull(from, "from");
    Assert.notNull(to, "to");
    Assert.notNull(inputStream, "inputStream");
    Assert.notNull(outputStream, "outputStream");

    try {
      translate();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  protected void translate() throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    BufferedWriter br = new BufferedWriter(new OutputStreamWriter(this.outputStream));
    String line = null;
    while ((line = reader.readLine()) != null) {
      if (StringUtils.isEmpty(line)) {
        continue;
      }
      Translate translate = new Translate(from, to, line);
      act(translate);
      br.write(line);
      br.write("\n");
      logger.info(line);
      if (null != translate.getTarget()) {
        br.write(translate.getTarget());
        br.write("\n\n");
        logger.info(translate.getTarget());
      }
      br.flush();
      if (sleep > 0) {
        try {
          Thread.sleep(new Random().nextInt(sleep) * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    closeQuiet(reader, br);
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

    HttpBuilder<String, String> httpBuilder = StringHttpClientBuilder //
        .newBuilder() //
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
  
  public GoogleHybirdTranslateBuilder target(OutputStream outputStream) {
    this.outputStream = outputStream;
    return this;
  }

  public GoogleHybirdTranslateBuilder start(long start) {
    this.start = start;
    return this;
  }
  
  /**
   * 
   * @param sleep ms
   */
  public GoogleHybirdTranslateBuilder sleep(int sleep) {
    this.sleep = sleep;
    return this;
  }

}
