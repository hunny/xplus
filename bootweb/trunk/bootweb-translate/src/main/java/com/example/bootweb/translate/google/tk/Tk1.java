package com.example.bootweb.translate.google.tk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Tk1 extends AbstractTk {

  private static final String SPLITOR = "/";

  @Override
  protected String getFunc() {
    return "tk";
  }

  @Override
  protected String getScript() {
    InputStream in = Tk1.class.getResourceAsStream(SPLITOR //
        + Tk1.class.getPackage().getName().replaceAll("\\.", SPLITOR) //
        + SPLITOR + "tk1.js");
    try {
      return new String(readStream(in), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  protected byte[] readStream(InputStream inStream) {
    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len = -1;
    try {
      while ((len = inStream.read(buffer)) != -1) {
        outSteam.write(buffer, 0, len);
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      try {
        outSteam.close();
      } catch (Exception e) {
      }
      try {
        inStream.close();
      } catch (Exception e) {
      }
    }
    return outSteam.toByteArray();
  }

  public static void main(String[] args) {
    Tk tk = new Tk1();
    System.out.println(tk.calc("Hello"));
  }

}
