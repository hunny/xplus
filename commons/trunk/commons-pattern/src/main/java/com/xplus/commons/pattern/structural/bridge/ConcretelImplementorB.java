package com.xplus.commons.pattern.structural.bridge;

public class ConcretelImplementorB implements Implementor {

  @Override
  public void operationImpl() {
    System.out.println("实现者B的操作方法。");
  }

}
