package com.example.bootweb.translate.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.api.Translate;
import com.example.bootweb.translate.profile.TranslateBasicDemo;

/**
 * https://www.cnblogs.com/wcymiss/p/6264847.html
 */
@Component
@TranslateBasicDemo
public class TranslateBasicDemoRunner implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    StringBuffer buffer = new StringBuffer();
    buffer.append("may be included more than once and specifies what to return in the reply.");
    buffer.append("Here are some values for dt. If the value is set, ");
    buffer.append("the following data will be returned:");

    Translate translate = GoogleTranslateBuilder.newBuilder() //
        .source(buffer.toString()) //
        .from(EN.class) //
        .to(CN.class) //
        .build();

    System.out.println(translate);
    System.out.println(translate.getText());
    System.out.println(translate.getTarget());

    if (args.length == 2) {// Read from file and write to file.
      System.out.println(Arrays.asList(args));
      File file = new File(args[1]);
      GoogleHybirdTranslateBuilder.newBuilder() //
          .from(EN.class) //
          .to(CN.class) //
          .source(new FileInputStream(args[0])) //
          .target(new FileOutputStream(file)) //
          .build() //
      ; //
    }
    System.exit(0);
  }

}
