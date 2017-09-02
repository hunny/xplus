package com.xplus.commons.pattern.structural.decorator;

public class ConcreteComponent implements Component {

  @Override
  public void operation() {
    System.out.println("组件内的具体实现方法。");
  }

}
