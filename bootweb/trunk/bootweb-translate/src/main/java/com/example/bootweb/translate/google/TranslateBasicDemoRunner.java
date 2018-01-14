package com.example.bootweb.translate.google;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.api.Params;
import com.example.bootweb.translate.profile.TranslateBasicDemo;

@Component
@TranslateBasicDemo
public class TranslateBasicDemoRunner implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    List<Params> params = GoogleParamsBuilder //
        .newBuilder(CN.class, EN.class) //
        .setText("Hello") //
        .build();//
    for (Params param : params) {
      System.out.println(param);
    }
    System.exit(0);
  }

}
