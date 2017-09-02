package com.xplus.commons.pattern.structural.bridge;

public class ConcretelImplementorA implements Implementor {

  @Override
  public void operationImpl() {
    System.out.println("实现者A的操作方法。");
  }

}
