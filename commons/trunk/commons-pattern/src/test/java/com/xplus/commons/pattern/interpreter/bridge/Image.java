package com.xplus.commons.pattern.interpreter.bridge;

/**
 * 抽象图像类：抽象类
 * @author huzexiong
 */
public abstract class Image {
  
  protected ImageImp imp;  

  public void setImageImp(ImageImp imp) {  
      this.imp = imp;  
  }   

  public abstract void parseFile(String fileName);
  
}
