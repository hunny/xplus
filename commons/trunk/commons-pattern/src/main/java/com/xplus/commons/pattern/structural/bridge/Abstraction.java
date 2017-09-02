package com.xplus.commons.pattern.structural.bridge;

public abstract class Abstraction {
  
  private Implementor impl; // 定义实现类接口对象

  public void setImpl(Implementor impl) {
    this.impl = impl;
  }

  public Implementor getImpl() {
    return impl;
  }

  public abstract void operation(); // 声明抽象业务方法
  
}

