package com.example.bootweb.translate.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
      OutputStream outputStream = GoogleHybirdTranslateBuilder.newBuilder() //
          .from(EN.class) //
          .to(CN.class) //
          .source(new FileInputStream(args[0])) //
          .build() //
      ; //
      toFile(outputStream, args[1]);
    }
    System.exit(0);
  }

  protected void toFile(OutputStream outputStream, String fileName) {
    File file = new File(fileName);
    try {
      byte[] buf = new byte[8192];
      InputStream is = new FileInputStream(file);
      int c = 0;
      while ((c = is.read(buf, 0, buf.length)) > 0) {
        outputStream.write(buf, 0, c);
        outputStream.flush();
      }
      is.close();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
