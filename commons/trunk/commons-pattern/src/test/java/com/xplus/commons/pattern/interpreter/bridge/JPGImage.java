package com.xplus.commons.pattern.interpreter.bridge;

/**
 * JPG格式图像：扩充抽象类
 * 
 * @author huzexiong
 */
public class JPGImage extends Image {

  @Override
  public void parseFile(String fileName) {
    // 模拟解析JPG文件并获得一个像素矩阵对象m;
    Matrix m = new Matrix();
    imp.doPaint(m);
    System.out.println(fileName + "，格式为JPG。");
  }

}
