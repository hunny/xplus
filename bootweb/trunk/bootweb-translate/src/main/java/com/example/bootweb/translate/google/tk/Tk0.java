package com.example.bootweb.translate.google.tk;

import java.io.IOException;
import java.io.InputStream;

public class Tk0 extends AbstractTk {

  private static final String SPLITOR = "/";
  
  @Override
  protected String getScript() {
    InputStream in = Tk0.class.getResourceAsStream(SPLITOR //
        + Tk0.class.getPackage().getName().replaceAll("\\.", SPLITOR) //
        + SPLITOR + "tk0.js");
    try {
      return from(in);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  protected String getFunc() {
    return "tk";
  }
  
  protected String from(InputStream inputStream) throws IOException {
    byte[] buffer = new byte[2048];
    int readBytes = 0;
    StringBuilder stringBuilder = new StringBuilder();
    while((readBytes = inputStream.read(buffer)) > 0){
        stringBuilder.append(new String(buffer, 0, readBytes));
    }
    return stringBuilder.toString();
  }
  
  public static void main(String[] args) {
    Tk tk = new Tk0();
    System.out.println(tk.calc("Hello World"));
  }

}
