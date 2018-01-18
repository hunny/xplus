package com.example.bootweb.translate.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.profile.TranslateBasicDemo;

/**
 * https://www.cnblogs.com/wcymiss/p/6264847.html
 */
@Component
@TranslateBasicDemo
public class TranslateBasicDemoRunner implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    if (args.length == 2) {// Read from file and write to file.
      System.out.println(Arrays.asList(args));
      File file = new File(args[1]);
      GoogleHybirdTranslateBuilder.newBuilder() //
          .from(EN.class) //
          .to(CN.class) //
          .sleep(5) // Random sleep <= 5s
          .source(new FileInputStream(args[0])) //
          .target(new FileOutputStream(file)) //
          .build() //
      ; //
      System.exit(0);
    }
  }

}
