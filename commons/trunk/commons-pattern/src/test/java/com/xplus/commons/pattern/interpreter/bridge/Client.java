package com.xplus.commons.pattern.interpreter.bridge;

public class Client {

  public static void main(String[] args) {
    Image image = new PNGImage();
    ImageImp imp = new LinuxImp();
    image.setImageImp(imp);
    image.parseFile("小龙女");
    
    image = new GIFImage();
    imp = new UnixImp();
    image.setImageImp(imp);
    image.parseFile("小龙女");
  }

}
