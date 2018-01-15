package com.example.bootweb.translate.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.profile.TranslateBasicDemo;
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
    Translate translate = new Translate(EN.class, //
        CN.class, //
        "may be included more than once and specifies what to return in the reply.Here are some values for dt. If the value is set, the following data will be returned:");
    translate = GoogleTranslateBuilder.newBuilder() //
        .setObjectMapper(objectMapper) //
        .setTranslate(translate) //
        .build();
    System.out.println(translate.getText());
    System.out.println(translate.getTarget());
    System.exit(0);
  }

}
