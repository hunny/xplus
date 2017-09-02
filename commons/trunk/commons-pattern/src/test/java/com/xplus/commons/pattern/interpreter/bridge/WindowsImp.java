package com.xplus.commons.pattern.interpreter.bridge;

/**
 * Windows操作系统实现类：具体实现类
 * @author huzexiong
 */
public class WindowsImp implements ImageImp {

  @Override
  public void doPaint(Matrix m) {
    // 调用Windows系统的绘制函数绘制像素矩阵
    System.out.print("在Windows操作系统中显示图像：");
  }

}
